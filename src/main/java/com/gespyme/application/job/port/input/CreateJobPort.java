package com.gespyme.application.job.port.input;

import com.gespyme.application.job.usecase.CreateJobUseCase;
import com.gespyme.commons.model.employee.EmployeeModelApi;
import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.domain.calendar.repository.UserByCalendarRepository;
import com.gespyme.domain.facade.EmployeeFacade;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateJobPort implements CreateJobUseCase {

  @Value("${calendar.adminEmail}")
  private String adminEmail;

  private final UserByCalendarRepository userByCalendarRepository;
  private final JobRepository repository;
  private final EmployeeFacade employeeFacade;
  private final JobByCalendarRepository jobByCalendarRepository;

  @Override
  public Job createJob(Job job) {
    EmployeeModelApi employeeModelApi = employeeFacade.getEmployeeById(job.getEmployeeId());
    List<String> calendars = new ArrayList<>();
    List<String> userCalendars =
        userByCalendarRepository.getUserByCalendarByUserEmail(employeeModelApi.getEmail()).stream()
            .map(UserByCalendar::getCalendarId)
            .toList();
    List<String> adminUserCalendars =
        userByCalendarRepository.getUserByCalendarByUserEmail(adminEmail).stream()
            .map(UserByCalendar::getCalendarId)
            .toList();
    calendars.addAll(userCalendars);
    calendars.addAll(adminUserCalendars);
    Job savedJob = repository.save(job);
    getJobsByCalendar(calendars, savedJob.getJobId()).forEach(jobByCalendarRepository::save);
    return savedJob;
  }

  private List<JobByCalendar> getJobsByCalendar(List<String> calendars, String jobId) {
    return calendars.stream()
        .map(userCalendar -> JobByCalendar.builder().jobId(jobId).calendarId(userCalendar).build())
        .toList();
  }
}
