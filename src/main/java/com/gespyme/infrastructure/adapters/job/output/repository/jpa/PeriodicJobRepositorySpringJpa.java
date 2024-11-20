package com.gespyme.infrastructure.adapters.job.output.repository.jpa;

import com.gespyme.infrastructure.adapters.job.output.model.entity.PeriodicJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PeriodicJobRepositorySpringJpa
    extends JpaRepository<PeriodicJobEntity, String>,
        QuerydslPredicateExecutor<PeriodicJobEntity> {}
