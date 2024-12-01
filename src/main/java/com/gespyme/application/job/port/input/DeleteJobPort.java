package com.gespyme.application.job.port.input;

import com.gespyme.application.job.usecase.DeleteJobUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteJobPort implements DeleteJobUseCase {

  private final JobRepository repository;

  @Override
  public void deleteJob(String jobId) {
    Job job = repository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
    repository.deleteById(job.getJobId());
  }
}
