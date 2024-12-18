package com.gespyme.infrastructure.adapters.calendar.output.repository;

import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.QCalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.QUserByCalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.repository.jpa.CalendarRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.CalendarMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarJpaRepository implements CalendarRepository {
  private final Map<String, QueryField> queryFieldMap;
  private final JPAQueryFactory queryFactory;
  private final CalendarMapper mapper;
  private final CalendarRepositorySpringJpa calendarRepositoryJpa;
  private final UserByCalendarJpaRepository userByCalendarJpaRepository;

  public CalendarJpaRepository(
      List<QueryField> queryFields,
      CalendarRepositorySpringJpa calendarRepositoryJpa,
      JPAQueryFactory queryFactory,
      CalendarMapper mapper,
      UserByCalendarJpaRepository userByCalendarJpaRepository) {
    this.calendarRepositoryJpa = calendarRepositoryJpa;
    this.queryFactory = queryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
    this.userByCalendarJpaRepository = userByCalendarJpaRepository;
  }

  public List<Calendar> findByCriteria(List<SearchCriteria> searchCriteria) {
    QCalendarEntity calendarEntity = QCalendarEntity.calendarEntity;
    QUserByCalendarEntity userByCalendar = QUserByCalendarEntity.userByCalendarEntity;
    JPAQuery<CalendarEntity> query =
        queryFactory
            .select(calendarEntity)
            .from(calendarEntity)
            .innerJoin(calendarEntity.users, userByCalendar);
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);

    return Objects.nonNull(booleanBuilder.getValue())
        ? executeQueryWithPredicate(calendarEntity, userByCalendar, booleanBuilder, query)
        : executeQueryWithoutPredicate(calendarEntity, userByCalendar, query);
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private List<Calendar> executeQueryWithPredicate(
      QCalendarEntity calendarEntity,
      QUserByCalendarEntity userByCalendar,
      BooleanBuilder booleanBuilder,
      JPAQuery<CalendarEntity> query) {
    List<Tuple> results =
        query.select(calendarEntity, userByCalendar).where(booleanBuilder).fetch();
    return getCalendars(calendarEntity, userByCalendar, results);
  }

  private List<Calendar> executeQueryWithoutPredicate(
      QCalendarEntity calendarEntity,
      QUserByCalendarEntity userByCalendar,
      JPAQuery<CalendarEntity> query) {
    List<Tuple> results =
        query
            .select(
                calendarEntity.calendarId,
                calendarEntity.calendarName,
                userByCalendar.userEmail,
                userByCalendar.userByCalendarId)
            .fetch();
    return getCalendars(calendarEntity, userByCalendar, results);
  }

  private ArrayList<Calendar> getCalendars(
      QCalendarEntity calendarEntity, QUserByCalendarEntity userByCalendar, List<Tuple> results) {
    Map<String, Calendar> calendarMap = new HashMap<>();

    for (Tuple tuple : results) {
      String calendarId = tuple.get(calendarEntity.calendarId);
      String calendarName = tuple.get(calendarEntity.calendarName);
      String userByCalendarId = tuple.get(userByCalendar.userByCalendarId);
      String userEmail = tuple.get(userByCalendar.userEmail);

      Calendar calendar = calendarMap.get(calendarId);
      if (calendar == null) {
        calendar = Calendar.builder().calendarId(calendarId).calendarName(calendarName).build();
        calendarMap.put(calendarId, calendar);
      }
      UserByCalendar userDTO =
          UserByCalendar.builder()
              .userByCalendarId(userByCalendarId)
              .calendarId(calendarId)
              .userEmail(userEmail)
              .build();

      calendar.addUser(userDTO);
    }

    return new ArrayList<>(calendarMap.values());
  }

  @Override
  public Optional<Calendar> findById(String id) {
    return calendarRepositoryJpa.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) {
    calendarRepositoryJpa.deleteById(id);
  }

  @Override
  public Calendar save(Calendar calendar) {
    CalendarEntity calendarEntity = mapper.mapToEntity(calendar);
    calendarRepositoryJpa.save(calendarEntity);
    return mapper.map(calendarEntity);
  }

  @Override
  public Calendar merge(Calendar newCalendarData, Calendar calendar) {
    Calendar merged = mapper.merge(newCalendarData, calendar);
    if(shouldChangeUsers(newCalendarData)) {
      List<UserByCalendar> users = getChangedUsers(newCalendarData, calendar);
      merged = mapper.merge(newCalendarData.toBuilder().users(users).build(), calendar);
    }
    CalendarEntity savedEntity =
        calendarRepositoryJpa.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }

  private List<UserByCalendar> getChangedUsers(
          Calendar newCalendarData, Calendar calendar) {
    List<UserByCalendar> newUsers =
            completeUsers(newCalendarData.getUsers(), calendar.getCalendarId());
    deleteChangedUsers(newUsers, calendar);
    return addCalendarUsers(newUsers);
  }

  private List<UserByCalendar> completeUsers(List<UserByCalendar> newUsers, String calendarId) {
    return newUsers.stream()
            .map(user -> user.toBuilder().calendarId(calendarId).build())
            .collect(Collectors.toList());
  }

  private List<UserByCalendar> addCalendarUsers(
      List<UserByCalendar> newUsers) {
    return newUsers.stream()
        .map(userByCalendarJpaRepository::save)
        .collect(Collectors.toList());
  }

  private void deleteChangedUsers(List<UserByCalendar> newUsers, Calendar calendar) {
    List<UserByCalendar> oldUsers = calendar.getUsers();
    List<String> toRemove = findDifference(oldUsers, newUsers);
    toRemove.forEach(userByCalendarJpaRepository::deleteById);
  }
  public List<String> findDifference(List<UserByCalendar> oldUsers, List<UserByCalendar> newUsers) {
    List<UserByCalendar> difference = new ArrayList<>(oldUsers);
    difference.removeAll(newUsers);
    return difference.stream()
        .map(UserByCalendar::getUserByCalendarId)
        .collect(Collectors.toList());
  }



  private boolean shouldChangeUsers(Calendar newCalendarData) {
    return Objects.nonNull(newCalendarData.getUsers());
  }

  private void manageUsers(Calendar newCalendarData) {

  }
}
