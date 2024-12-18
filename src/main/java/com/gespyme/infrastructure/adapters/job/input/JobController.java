package com.gespyme.infrastructure.adapters.job.input;

import com.gespyme.application.job.usecase.*;
import com.gespyme.commons.model.job.*;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.filter.JobFilter;
import com.gespyme.infrastructure.mapper.JobMapper;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
  private final JobMapper jobMapper;
  private final FindJobByIdUseCase findJobByIdUseCase;
  private final FindJobsUseCase findJobsUseCase;
  private final DeleteJobUseCase deleteJobUseCase;
  private final CreateJobUseCase createJobUseCase;
  private final ModifyJobUseCase modifyJobUseCase;
  private final ValidatorService<JobBaseModelApi> validatorService;

  @GetMapping("/{jobId}")
  public ResponseEntity<JobModelApi> getJobById(@PathVariable("jobId") String jobId) {
    Job job = findJobByIdUseCase.getJobById(jobId);
    return ResponseEntity.ok(jobMapper.map(job));
  }

  @GetMapping("/")
  public ResponseEntity<List<JobModelApi>> findJobs(JobFilterModelApi jobFilterModelApi) {
    validatorService.validate(jobFilterModelApi, List.of(Validator.ONE_PARAM_NOT_NULL));
    JobFilter jobFilter = jobMapper.map(jobFilterModelApi);
    List<Job> jobs = findJobsUseCase.findJobs(jobFilter, false);
    return ResponseEntity.ok(jobMapper.map(jobs));
  }

  @DeleteMapping("/{jobId}")
  public void deleteJob(@PathVariable("jobId") String jobId) {
    deleteJobUseCase.deleteJob(jobId);
  }

  @PostMapping
  public ResponseEntity<JobModelApi> createJob(@RequestBody JobModelApi jobApiModel) {
    validatorService.validate(jobApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    Job job = createJobUseCase.createJob(jobMapper.map(jobApiModel));
    URI location = URI.create("/job/" + job.getJobId());
    return ResponseEntity.created(location).body(jobMapper.map(job));
  }

  @PatchMapping("/{jobId}")
  public ResponseEntity<JobModelApi> modifyJob(
      @PathVariable("jobId") String jobId, @RequestBody JobModelApi jobApiModel) {
    validatorService.validateId(jobId);
    validatorService.validate(jobApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    Job job = modifyJobUseCase.modifyJob(jobId, jobMapper.map(jobApiModel));
    return ResponseEntity.ok(jobMapper.map(job));
  }

  @GetMapping("/findPendingJobs")
  public ResponseEntity<List<JobModelApi>> findPeriodicJobs(JobFilterModelApi jobFilterModelApi) {
    validatorService.validate(jobFilterModelApi, List.of(Validator.ONE_PARAM_NOT_NULL));
    JobFilter jobFilter = jobMapper.map(jobFilterModelApi);
    List<Job> calendars = findJobsUseCase.findJobs(jobFilter, true);
    return ResponseEntity.ok(jobMapper.map(calendars));
  }
}
