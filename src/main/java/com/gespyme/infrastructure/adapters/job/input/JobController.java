package com.gespyme.infrastructure.adapters.job.input;

import com.gespyme.application.job.usecase.CreateJobUseCase;
import com.gespyme.application.job.usecase.DeleteJobUseCase;
import com.gespyme.application.job.usecase.FindJobByIdUseCase;
import com.gespyme.application.job.usecase.ModifyJobUseCase;
import com.gespyme.commons.model.job.JobModelApi;
import com.gespyme.domain.job.model.Job;
import com.gespyme.infrastructure.mapper.JobMapper;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
  private final JobMapper jobMapper;
  private final FindJobByIdUseCase findJobByIdUseCase;
  private final DeleteJobUseCase deleteJobUseCase;
  private final CreateJobUseCase createJobUseCase;
  private final ModifyJobUseCase modifyJobUseCase;

  @GetMapping("/{jobId}")
  public JobModelApi getJobById(@PathParam("jobId") String jobId) {
    Job job = findJobByIdUseCase.getJobById(jobId);
    return jobMapper.map(job);
  }

  @DeleteMapping("/{jobId}")
  public void deleteJob(@PathParam("jobId") String jobId) {
    deleteJobUseCase.deleteJob(jobId);
  }

  @PostMapping("/")
  public JobModelApi createJob(JobModelApi jobApiModel) {
    Job job = createJobUseCase.createJob(jobMapper.map(jobApiModel));
    return jobMapper.map(job);
  }

  @PatchMapping("/")
  public JobModelApi modifyJob(JobModelApi jobApiModel) {
    Job job = modifyJobUseCase.modifyJob(jobMapper.map(jobApiModel));
    return jobMapper.map(job);
  }
}
