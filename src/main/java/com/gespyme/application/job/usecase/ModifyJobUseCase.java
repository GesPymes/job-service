package com.gespyme.application.job.usecase;

import com.gespyme.domain.job.model.Job;

public interface ModifyJobUseCase {
  Job modifyJob(String jobId, Job job);
}
