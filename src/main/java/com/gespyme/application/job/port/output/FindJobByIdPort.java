package com.gespyme.application.job.port.output;

import com.gespyme.application.job.usecase.FindJobByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.job.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindJobByIdPort implements FindJobByIdUseCase {
  private final GenericRepository<Job> repository;

  @Override
  public Job getJobById(String jobId) {
    return repository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
  }
}
