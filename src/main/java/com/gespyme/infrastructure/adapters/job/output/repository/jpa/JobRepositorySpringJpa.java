package com.gespyme.infrastructure.adapters.job.output.repository.jpa;

import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JobRepositorySpringJpa
    extends JpaRepository<JobEntity, String>, QuerydslPredicateExecutor<JobEntity> {}
