package com.gespyme.application.job.usecase;

import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.filter.JobFilter;
import java.util.List;

public interface FindJobsUseCase {
  List<Job> findJobs(JobFilter jobFilter, boolean isPeriodicBatchCall);
}
