package com.gespyme.application.job.port.input;

import com.gespyme.application.job.usecase.ModifyJobUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.job.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyJobPort implements ModifyJobUseCase {

  private final GenericRepository<Job> repository;

  @Override
  public Job modifyJob(Job newJob) {
    Job job =
        repository
            .findById(newJob.getJobId())
            .orElseThrow(() -> new NotFoundException("Job not found"));
    return repository.merge(newJob, job);
  }
}
