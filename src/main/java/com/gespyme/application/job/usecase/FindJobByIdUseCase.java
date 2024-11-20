package com.gespyme.application.job.usecase;

import com.gespyme.domain.job.model.Job;

public interface FindJobByIdUseCase {
  Job getJobById(String jobId);
}
