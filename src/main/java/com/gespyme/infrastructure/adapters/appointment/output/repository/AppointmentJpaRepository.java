package com.gespyme.infrastructure.adapters.appointment.output.repository;

import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.QAppointmentEntity;
import com.gespyme.infrastructure.adapters.appointment.output.repository.jpa.AppointmentRepositorySpringJpa;
import com.gespyme.infrastructure.adapters.job.output.model.entity.QJobEntity;
import com.gespyme.infrastructure.mapper.AppointmentMapper;
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
public class AppointmentJpaRepository implements AppointmentRepository {
  private final Map<String, QueryField> queryFieldMap;
  private final JPAQueryFactory queryFactory;
  private final AppointmentMapper mapper;
  private final AppointmentRepositorySpringJpa appointmentRepositorySpringJpa;

  public AppointmentJpaRepository(
      List<QueryField> queryFields,
      AppointmentRepositorySpringJpa appointmentRepositorySpringJpa,
      JPAQueryFactory queryFactory,
      AppointmentMapper mapper) {
    this.appointmentRepositorySpringJpa = appointmentRepositorySpringJpa;
    this.queryFactory = queryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
  }

  @Override
  public List<Appointment> findByCriteria(List<SearchCriteria> searchCriteria) {
    QAppointmentEntity appointmentEntity = QAppointmentEntity.appointmentEntity;
    QJobEntity jobEntity = QJobEntity.jobEntity;
    JPAQuery<AppointmentEntity> query =
        queryFactory.select(appointmentEntity).from(appointmentEntity);
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    List<Tuple> result =
        Objects.nonNull(booleanBuilder.getValue())
            ? executeQueryWithPredicate(appointmentEntity, jobEntity, booleanBuilder, query)
            : executeQueryWithoutPredicate(appointmentEntity, jobEntity, query);
    return result.stream()
        .map(tuple -> mapTuple(tuple, appointmentEntity, jobEntity))
        .collect(Collectors.toList());
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private List<Tuple> executeQueryWithPredicate(
      QAppointmentEntity appointment,
      QJobEntity job,
      BooleanBuilder booleanBuilder,
      JPAQuery<AppointmentEntity> query) {
    return query
        .select(
            appointment.status,
            appointment.startDate,
            appointment.endDate,
            job.periodicity,
            job.isPeriodic,
            job.descripton)
        .where(booleanBuilder)
        .fetch();
  }

  private List<Tuple> executeQueryWithoutPredicate(
      QAppointmentEntity appointment, QJobEntity job, JPAQuery<AppointmentEntity> query) {
    return query
        .select(
            appointment.status,
            appointment.startDate,
            appointment.endDate,
            job.periodicity,
            job.isPeriodic,
            job.descripton)
        .fetch();
  }

  private Appointment mapTuple(Tuple tuple, QAppointmentEntity appointment, QJobEntity job) {
    return Appointment.builder()
        .status(tuple.get(appointment.status))
        .endDate(tuple.get(appointment.startDate))
        .startDate(tuple.get(appointment.endDate))
        .periodicity(tuple.get(job.periodicity))
        .isPeriodic(tuple.get(job.isPeriodic))
        .description(tuple.get(job.descripton))
        .build();
  }

  @Override
  public Optional<Appointment> findById(String id) {
    return appointmentRepositorySpringJpa.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) {
    appointmentRepositorySpringJpa.deleteById(id);
  }

  @Override
  public Appointment save(Appointment job) {
    AppointmentEntity jobEntity = appointmentRepositorySpringJpa.save(mapper.mapToEntity(job));
    return mapper.map(jobEntity);
  }

  @Override
  public Appointment merge(Appointment newAppointmentData, Appointment job) {
    Appointment merged = mapper.merge(newAppointmentData, job);
    AppointmentEntity savedEntity = appointmentRepositorySpringJpa.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }
}
