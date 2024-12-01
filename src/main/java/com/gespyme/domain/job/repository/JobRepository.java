package com.gespyme.domain.job.repository;

import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.job.model.Job;
import java.util.List;

public interface JobRepository extends GenericRepository<Job> {
  List<Job> findByCriteria(List<SearchCriteria> searchCriteria);

  List<Job> findPendingPeriodicJobs(List<SearchCriteria> searchCriteria);
}
