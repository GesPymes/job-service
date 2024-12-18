package com.gespyme.infrastructure.adapters.job.output.repository;

import com.gespyme.commons.exeptions.BadRequestException;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobRepository;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.PeriodicJobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.QJobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.QPeriodicJobEntity;
import com.gespyme.infrastructure.adapters.job.output.repository.jpa.JobRepositorySpringJpa;
import com.gespyme.infrastructure.adapters.job.output.repository.jpa.PeriodicJobRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.JobMapper;
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
public class JobRepositoryJpa implements JobRepository {

  private final Map<String, QueryField> queryFieldMap;
  private final JPAQueryFactory jobQueryFactory;
  private final JPAQueryFactory periodicJobQueryFactory;
  private final JobMapper mapper;
  private final JobRepositorySpringJpa jobRepositorySpringJpa;
  private final PeriodicJobRepositorySpringJpa periodicJobRepositorySpringJpa;

  public JobRepositoryJpa(
      List<QueryField> queryFields,
      JobRepositorySpringJpa jobRepositorySpringJpa,
      JPAQueryFactory jobQueryFactory,
      JPAQueryFactory periodicJobQueryFactory,
      JobMapper mapper,
      PeriodicJobRepositorySpringJpa periodicJobRepositorySpringJpa) {
    this.jobRepositorySpringJpa = jobRepositorySpringJpa;
    this.jobQueryFactory = jobQueryFactory;
    this.periodicJobQueryFactory = periodicJobQueryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
    this.periodicJobRepositorySpringJpa = periodicJobRepositorySpringJpa;
  }

  @Override
  public List<Job> findByCriteria(List<SearchCriteria> searchCriteria) {
    QJobEntity jobEntity = QJobEntity.jobEntity;
    QPeriodicJobEntity periodicJobEntity = QPeriodicJobEntity.periodicJobEntity;
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    return getJobs(searchCriteria, jobEntity, periodicJobEntity, booleanBuilder);
  }

  @Override
  public List<Job> findPendingPeriodicJobs(List<SearchCriteria> searchCriteria) {
    QPeriodicJobEntity periodicJobEntity = QPeriodicJobEntity.periodicJobEntity;
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    List<PeriodicJobEntity> entities =
        jobQueryFactory
            .select(periodicJobEntity)
            .where(booleanBuilder)
            .from(periodicJobEntity)
            .fetch();
    return mapper.mapEntityList(entities);
  }

  private List<Job> getJobs(
      List<SearchCriteria> searchCriteria,
      QJobEntity jobEntity,
      QPeriodicJobEntity periodicJobEntity,
      BooleanBuilder booleanBuilder) {
    Optional<SearchCriteria> isPeriodic =
        searchCriteria.stream().filter(x -> "isPeriodic".equals(x.getKey())).findFirst();
    if (isPeriodic.isPresent()) {
      if (isPeriodic.get().getValue().equals(Boolean.TRUE)) {
        return getJobs(periodicJobEntity, booleanBuilder);
      } else {
        return getTupleFromJob(jobEntity, booleanBuilder);
      }
    }
    List<Job> periodicJobTuple = getJobs(periodicJobEntity, booleanBuilder);
    List<Job> jobTuple = getTupleFromJob(jobEntity, booleanBuilder);
    periodicJobTuple.addAll(jobTuple);
    return periodicJobTuple;
  }

  private List<Job> getTupleFromJob(QJobEntity jobEntity, BooleanBuilder booleanBuilder) {
    JPAQuery<Tuple> query =
        jobQueryFactory
            .select(
                jobEntity.jobId, jobEntity.description, jobEntity.employeeId, jobEntity.customerId)
            .from(jobEntity);
    List<Tuple> tuples =
        Objects.nonNull(booleanBuilder.getValue())
            ? query.where(booleanBuilder).fetch()
            : query.fetch();
    return tuples.stream().map(tuple -> mapTuple(tuple, jobEntity)).collect(Collectors.toList());
  }

  private List<Job> getJobs(QPeriodicJobEntity periodicJobEntity, BooleanBuilder booleanBuilder) {
    JPAQuery<Tuple> query =
        periodicJobQueryFactory
            .select(
                periodicJobEntity.jobId,
                periodicJobEntity.description,
                periodicJobEntity.periodicity,
                periodicJobEntity.employeeId,
                periodicJobEntity.customerId)
            .from(periodicJobEntity);
    List<Tuple> tuples =
        Objects.nonNull(booleanBuilder.getValue())
            ? query.where(booleanBuilder).fetch()
            : query.fetch();
    return tuples.stream()
        .map(tuple -> mapPeriodicTuple(tuple, periodicJobEntity))
        .collect(Collectors.toList());
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private Job mapPeriodicTuple(Tuple tuple, QPeriodicJobEntity periodicJob) {
    return Job.builder()
        .jobId(tuple.get(periodicJob.jobId))
        .employeeId(tuple.get(periodicJob.employeeId))
        .customerId(tuple.get(periodicJob.customerId))
        .periodicity(tuple.get(periodicJob.periodicity))
        .isPeriodic(true)
        .description(tuple.get(periodicJob.description))
        .build();
  }

  private Job mapTuple(Tuple tuple, QJobEntity job) {
    return Job.builder()
        .jobId(tuple.get(job.jobId))
        .employeeId(tuple.get(job.employeeId))
        .customerId(tuple.get(job.customerId))
        .description(tuple.get(job.description))
        .isPeriodic(false)
        .build();
  }

  @Override
  public Optional<Job> findById(String id) {
    Optional<JobEntity> jobEntity = jobRepositorySpringJpa.findById(id);
    return jobEntity.isPresent()
        ? jobEntity.map(mapper::map).map(x -> x.withIsPeriodic(false))
        : periodicJobRepositorySpringJpa
            .findById(id)
            .map(mapper::map)
            .map(x -> x.withIsPeriodic(true));
  }

  @Override
  public void deleteById(String id) throws NotFoundException {
    Job job = findById(id).orElseThrow(() -> new NotFoundException("Job not found"));
    if (job.getIsPeriodic()) {
      periodicJobRepositorySpringJpa.deleteById(id);
    }
    jobRepositorySpringJpa.deleteById(id);
  }

  @Override
  public Job save(Job job) {
    Boolean isPeriodic =
        Optional.ofNullable(job.getIsPeriodic())
            .orElseThrow(() -> new BadRequestException("Periodic field must be informed"));
    return isPeriodic
        ? mapper.map(periodicJobRepositorySpringJpa.save(mapper.mapToPeriodicEntity(job)))
        : mapper.map(jobRepositorySpringJpa.save(mapper.mapToEntity(job)));
  }

  @Override
  public Job merge(Job newJobData, Job job) {
    Job merged = mapper.merge(newJobData, job);
    Boolean isPeriodic =
        Optional.ofNullable(merged.getIsPeriodic())
            .orElseThrow(() -> new BadRequestException("Periodic field must be informed"));
    return isPeriodic
        ? mapper.map(periodicJobRepositorySpringJpa.save(mapper.mapToPeriodicEntity(job)))
        : mapper.map(jobRepositorySpringJpa.save(mapper.mapToEntity(job)));
  }
}
