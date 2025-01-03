package com.gespyme.application.job.port.output;

import com.gespyme.application.job.usecase.FindJobsUseCase;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.filter.JobFilter;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindJobsPort implements FindJobsUseCase {
  private final JobRepository jobRepository;
  private final List<FieldFilter<JobFilter>> filters;

  @Override
  public List<Job> findJobs(JobFilter jobFilter, boolean isPeriodicBatchCall) {
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    filters.stream()
        .filter(f -> f.apply(jobFilter))
        .forEach(f -> f.addSearchCriteria(jobFilter, searchCriterias));
    return jobRepository.findByCriteria(searchCriterias);
  }
}
