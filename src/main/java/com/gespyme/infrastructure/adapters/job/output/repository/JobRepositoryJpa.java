package com.gespyme.infrastructure.adapters.job.output.repository;

import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobRepository;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.QJobEntity;
import com.gespyme.infrastructure.adapters.job.output.repository.jpa.JobRepositorySpringJpa;
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

  private final Map<String, QueryField<JobEntity>> queryFieldMap;
  private final JPAQueryFactory jobQueryFactory;
  private final JobMapper mapper;
  private final JobRepositorySpringJpa jobRepositorySpringJpa;

  public JobRepositoryJpa(
      List<QueryField<JobEntity>> queryFields,
      JobRepositorySpringJpa jobRepositorySpringJpa,
      JPAQueryFactory jobQueryFactory,
      JobMapper mapper) {
    this.jobRepositorySpringJpa = jobRepositorySpringJpa;
    this.jobQueryFactory = jobQueryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
  }

  @Override
  public List<Job> findByCriteria(List<SearchCriteria> searchCriteria) {
    QJobEntity jobEntity = QJobEntity.jobEntity;
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    return getTupleFromJob(jobEntity, booleanBuilder);
  }

  private List<Job> getTupleFromJob(QJobEntity jobEntity, BooleanBuilder booleanBuilder) {
    JPAQuery<Tuple> query =
        jobQueryFactory
            .select(
                jobEntity.jobId,
                jobEntity.description,
                jobEntity.employeeId,
                jobEntity.customerId,
                jobEntity.periodicity,
                jobEntity.periodic)
            .from(jobEntity);
    List<Tuple> tuples =
        Objects.nonNull(booleanBuilder.getValue())
            ? query.where(booleanBuilder).fetch()
            : query.fetch();
    return tuples.stream().map(tuple -> mapTuple(tuple, jobEntity)).collect(Collectors.toList());
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private Job mapTuple(Tuple tuple, QJobEntity job) {
    return Job.builder()
        .jobId(tuple.get(job.jobId))
        .employeeId(tuple.get(job.employeeId))
        .customerId(tuple.get(job.customerId))
        .description(tuple.get(job.description))
        .periodicity(tuple.get(job.periodicity))
        .periodic(tuple.get(job.periodic))
        .build();
  }

  @Override
  public Optional<Job> findById(String id) {
    Optional<JobEntity> jobEntity = jobRepositorySpringJpa.findById(id);
    return jobEntity.map(mapper::map);
  }

  @Override
  public void deleteById(String id) throws NotFoundException {
    jobRepositorySpringJpa.deleteById(id);
  }

  @Override
  public Job save(Job job) {
    return mapper.map(jobRepositorySpringJpa.save(mapper.mapToEntity(job)));
  }

  @Override
  public Job merge(Job newJobData, Job job) {
    Job merged = mapper.merge(newJobData, job);
    return mapper.map(jobRepositorySpringJpa.save(mapper.mapToEntity(merged)));
  }
}
