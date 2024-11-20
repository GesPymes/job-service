package com.gespyme.application.job.port.input;

import com.gespyme.application.job.usecase.CreateJobUseCase;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.job.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateJobPort implements CreateJobUseCase {

  private final GenericRepository<Job> repository;

  @Override
  public Job createJob(Job job) {
    return repository.save(job);
  }
}
