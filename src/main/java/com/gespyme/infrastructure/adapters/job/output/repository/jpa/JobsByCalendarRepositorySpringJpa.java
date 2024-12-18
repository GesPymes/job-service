package com.gespyme.infrastructure.adapters.job.output.repository.jpa;

import com.gespyme.infrastructure.adapters.job.output.model.entity.JobByCalendarEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JobsByCalendarRepositorySpringJpa
    extends JpaRepository<JobByCalendarEntity, String>,
        QuerydslPredicateExecutor<JobByCalendarEntity> {
  List<JobByCalendarEntity> findByJobId(String jobId);
}
