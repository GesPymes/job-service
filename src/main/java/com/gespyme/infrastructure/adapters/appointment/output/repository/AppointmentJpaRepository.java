package com.gespyme.infrastructure.adapters.appointment.output.repository;

import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.QAppointmentEntity;
import com.gespyme.infrastructure.adapters.appointment.output.repository.jpa.AppointmentRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.AppointmentMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentJpaRepository implements AppointmentRepository {
  private final AppointmentMapper mapper;
  private final AppointmentRepositorySpringJpa appointmentRepositorySpringJpa;
  private final Map<String, QueryField> queryFieldMap;
  private final JPAQueryFactory jobQueryFactory;

  public AppointmentJpaRepository(
      AppointmentMapper mapper,
      AppointmentRepositorySpringJpa appointmentRepositorySpringJpa,
      List<QueryField> queryFields,
      JPAQueryFactory jobQueryFactory) {
    this.mapper = mapper;
    this.appointmentRepositorySpringJpa = appointmentRepositorySpringJpa;
    this.queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
    this.jobQueryFactory = jobQueryFactory;
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

  @Override
  public List<Appointment> findByCriteria(List<SearchCriteria> searchCriteria) {
    QAppointmentEntity appointmentEntity = QAppointmentEntity.appointmentEntity;

    List<Tuple> result = getTuple(searchCriteria, appointmentEntity);

    return result.stream()
        .map(tuple -> mapTuple(tuple, appointmentEntity))
        .collect(Collectors.toList());
  }

  private List<Tuple> getTuple(
      List<SearchCriteria> searchCriteria, QAppointmentEntity appointmentEntity) {
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    JPAQuery<Tuple> query =
        jobQueryFactory
            .select(
                appointmentEntity.appointmentId,
                appointmentEntity.jobId,
                appointmentEntity.status,
                appointmentEntity.startDate,
                appointmentEntity.endDate)
            .from(appointmentEntity)
            .where(booleanBuilder);
    return query.fetch();
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private Appointment mapTuple(Tuple tuple, QAppointmentEntity appointment) {
    return Appointment.builder()
        .jobId(tuple.get(appointment.jobId))
        .appointmentId(tuple.get(appointment.appointmentId))
        .status(tuple.get(appointment.status))
        .endDate(tuple.get(appointment.endDate))
        .startDate(tuple.get(appointment.startDate))
        .build();
  }
}
