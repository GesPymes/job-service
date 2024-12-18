package com.gespyme.application.job.port.input;

import com.gespyme.application.job.usecase.ModifyJobUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.employee.EmployeeModelApi;
import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.domain.calendar.repository.UserByCalendarRepository;
import com.gespyme.domain.facade.EmployeeFacade;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyJobPort implements ModifyJobUseCase {

  private final UserByCalendarRepository userByCalendarRepository;
  private final JobByCalendarRepository jobByCalendarRepository;
  private final JobRepository repository;
  private final EmployeeFacade employeeFacade;

  @Override
  public Job modifyJob(String jobId, Job newJob) {
    Job job = repository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
    if (Objects.nonNull(newJob.getEmployeeId())
        && !Objects.equals(newJob.getEmployeeId(), job.getEmployeeId())) {
      updateEmployeeCalendar(jobId, job);
    }
    performPeriodicTableChange(job, newJob);
    Job merged = repository.merge(newJob, job);

    return merged;
  }

  private void performPeriodicTableChange(Job job, Job newJob) {
    if (Objects.nonNull(newJob.getIsPeriodic()) && newJob.getIsPeriodic() != job.getIsPeriodic()) {
      repository.deleteById(job.getJobId());
    }
  }

  private void updateEmployeeCalendar(String jobId, Job job) {
    EmployeeModelApi employeeModelApi = employeeFacade.getEmployeeById(job.getEmployeeId());
    List<String> userCalendars =
        userByCalendarRepository.getUserByCalendarByUserEmail(employeeModelApi.getEmail()).stream()
            .map(UserByCalendar::getCalendarId)
            .toList();
    deleteJobsByCalendar(jobId);
    userCalendars.stream()
        .map(userCalendar -> JobByCalendar.builder().jobId(jobId).calendarId(userCalendar).build())
        .forEach(jobByCalendarRepository::save);
  }

  private void deleteJobsByCalendar(String jobId) {
    List<JobByCalendar> jobsByCalendar = jobByCalendarRepository.getCalendarsByJobId(jobId);
    jobsByCalendar.stream()
        .map(JobByCalendar::getJobByCalendarId)
        .forEach(jobByCalendarRepository::delete);
  }

  private List<JobByCalendar> getJobsByCalendar(List<String> calendars, String jobId) {
    return calendars.stream()
        .map(userCalendar -> JobByCalendar.builder().jobId(jobId).calendarId(userCalendar).build())
        .toList();
  }
}
