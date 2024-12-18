package com.gespyme.application.job.port.input;

import com.gespyme.application.appointment.port.output.facade.CustomerFacade;
import com.gespyme.application.job.usecase.CreateJobUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.customer.CustomerModelApi;
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
import java.util.Objects;
import java.util.Optional;

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
  private final CustomerFacade customerFacade;

  @Override
  public Job createJob(Job job) {
    EmployeeModelApi employeeModelApi = getEmployeeModelApi(job);
    addCustomerId(job);
    addEmployeeId(job, employeeModelApi);
    List<String> calendars = getCalendars(employeeModelApi.getEmail());

    Job savedJob = repository.save(job);
    getJobsByCalendar(calendars, savedJob.getJobId()).forEach(jobByCalendarRepository::save);
    return savedJob;
  }

  private List<JobByCalendar> getJobsByCalendar(List<String> calendars, String jobId) {
    return calendars.stream()
        .map(userCalendar -> JobByCalendar.builder().jobId(jobId).calendarId(userCalendar).build())
        .toList();
  }

  private void addCustomerId(Job job) {
    if (Objects.nonNull(job.getCustomerName()) && Objects.nonNull(job.getCustomerLastName())) {
      CustomerModelApi customerModelApi =
          customerFacade.getCustomers(job.getCustomerName(), job.getCustomerLastName()).stream()
              .findFirst()
              .orElseThrow(() -> new NotFoundException("Customer exception"));
      job.setCustomerId(customerModelApi.getCustomerId());
    }
  }

  private void addEmployeeId(Job job, EmployeeModelApi employeeModelApi) {
    if (Objects.nonNull(job.getEmployeeName())) {
      job.setEmployeeId(employeeModelApi.getEmployeeId());
    }
  }

  private EmployeeModelApi getEmployeeModelApi(Job job) {
    if (Objects.nonNull(job.getEmployeeName())) {
      return employeeFacade.getEmployees(job.getEmployeeName(), job.getCustomerLastName()).stream()
          .findFirst()
          .orElseThrow(() -> new NotFoundException("Employee exception"));
    }
    return Optional.ofNullable(job.getEmployeeId())
        .map(employeeFacade::getEmployeeById)
        .orElseThrow(() -> new NotFoundException("Employee not found"));
  }

  private List<String> getCalendars(String email) {
    List<String> calendars = new ArrayList<>();
    List<String> userCalendars = getUsersCalendars(email);

    List<String> adminUserCalendars = getAdminsCalendars();

    calendars.addAll(userCalendars);
    calendars.addAll(adminUserCalendars);
    return calendars;
  }

  private List<String> getUsersCalendars(String email) {
    return userByCalendarRepository.getUserByCalendarByUserEmail(email).stream()
        .map(UserByCalendar::getCalendarId)
        .toList();
  }

  private List<String> getAdminsCalendars() {
    return userByCalendarRepository.getUserByCalendarByUserEmail(adminEmail).stream()
        .map(UserByCalendar::getCalendarId)
        .toList();
  }
}
