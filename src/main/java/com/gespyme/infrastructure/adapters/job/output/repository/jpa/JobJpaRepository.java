package com.gespyme.infrastructure.adapters.job.output.repository.jpa;

import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.job.model.Job;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import com.gespyme.infrastructure.mapper.JobMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JobJpaRepository implements GenericRepository<Job> {
  private final JobMapper mapper;
  private final JobRepositorySpringJpa jobRepositorySpringJpa;

  @Override
  public Optional<Job> findById(String id) {
    return jobRepositorySpringJpa.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) {
    jobRepositorySpringJpa.deleteById(id);
  }

  @Override
  public Job save(Job job) {
    JobEntity jobEntity = jobRepositorySpringJpa.save(mapper.mapToEntity(job));
    return mapper.map(jobEntity);
  }

  @Override
  public Job merge(Job newJobData, Job job) {
    Job merged = mapper.merge(newJobData, job);
    JobEntity savedEntity = jobRepositorySpringJpa.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }
}
