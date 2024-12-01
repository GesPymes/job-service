package com.gespyme.application.job.port.output;

import com.gespyme.application.job.usecase.FindJobByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindJobByIdPort implements FindJobByIdUseCase {
  private final JobRepository repository;

  @Override
  public Job getJobById(String jobId) {
    return repository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
  }
}
