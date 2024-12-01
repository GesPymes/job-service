package com.gespyme.infrastructure.adapters.calendar.output.repository;

import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.QCalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.repository.jpa.CalendarRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.CalendarMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarJpaRepository implements CalendarRepository {
  private final Map<String, QueryField> queryFieldMap;
  private final JPAQueryFactory queryFactory;
  private final CalendarMapper mapper;
  private final CalendarRepositorySpringJpa calendarRepositoryJpa;

  public CalendarJpaRepository(
      List<QueryField> queryFields,
      CalendarRepositorySpringJpa calendarRepositoryJpa,
      JPAQueryFactory queryFactory,
      CalendarMapper mapper) {
    this.calendarRepositoryJpa = calendarRepositoryJpa;
    this.queryFactory = queryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
  }

  public List<Calendar> findByCriteria(List<SearchCriteria> searchCriteria) {
    QCalendarEntity calendar = QCalendarEntity.calendarEntity;
    JPAQuery<CalendarEntity> query = queryFactory.select(calendar).from(calendar);
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    List<Tuple> result =
        Objects.nonNull(booleanBuilder.getValue())
            ? executeQueryWithPredicate(calendar, booleanBuilder, query)
            : executeQueryWithoutPredicate(calendar, query);
    return result.stream().map(tuple -> mapTuple(tuple, calendar)).collect(Collectors.toList());
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private List<Tuple> executeQueryWithPredicate(
      QCalendarEntity calendarEntity,
      BooleanBuilder booleanBuilder,
      JPAQuery<CalendarEntity> query) {
    return query
        .select(calendarEntity.calendarId, calendarEntity.calendarName)
        .where(booleanBuilder)
        .fetch();
  }

  private List<Tuple> executeQueryWithoutPredicate(
      QCalendarEntity calendarEntity, JPAQuery<CalendarEntity> query) {
    return query.select(calendarEntity.calendarId, calendarEntity.calendarName).fetch();
  }

  private Calendar mapTuple(Tuple tuple, QCalendarEntity calendarEntity) {
    return Calendar.builder()
        .calendarId(tuple.get(calendarEntity.calendarId))
        .calendarName(tuple.get(calendarEntity.calendarName))
        .build();
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
    CalendarEntity savedEntity = calendarRepositoryJpa.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }
}
