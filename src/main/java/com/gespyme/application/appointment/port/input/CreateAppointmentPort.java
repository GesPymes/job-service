package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.CreateAppointmentUseCase;
import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.domain.job.repository.JobRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAppointmentPort implements CreateAppointmentUseCase {
  private final AppointmentRepository repository;
  private final JobRepository jobRepository;
  private final JobByCalendarRepository jobByCalendarRepository;
  private final CalendarService calendarService;

  @Override
  public List<Appointment> createAppointment(String jobId, Appointment appointment) {
    Job job =
        jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
    List<JobByCalendar> jobByCalendars = jobByCalendarRepository.getCalendarsByJobId(jobId);
    checkAppointmentDates(appointment, job);

    List<Appointment> appointments = new ArrayList<>();

    jobByCalendars.forEach(
        calendar -> {
          Optional<String> id = createEvent(job, calendar, appointment);
          Appointment savedAppointment = savedAppointment(appointment, calendar, id);
          appointments.add(savedAppointment);
        });
    return appointments;
  }

  private Optional<String> createEvent(Job job, JobByCalendar calendar, Appointment appointment) {
    return calendarService.createCalendarEvent(
        job.getDescription(),
        calendar.getCalendarId(),
        appointment.getStartDate(),
        appointment.getEndDate());
  }

  private Appointment savedAppointment(
      Appointment appointment, JobByCalendar calendar, Optional<String> id) {
    return repository.save(
        appointment.toBuilder()
            .calendarId(calendar.getCalendarId())
            .appointmentId(id.orElseThrow(() -> new InternalServerError("Exception saving event")))
            .build());
  }

  private void checkAppointmentDates(Appointment newAppointment, Job job) {
    job.getAppointmentList().stream().forEach(appointment -> isDateOccupied(newAppointment.getStartDate(), newAppointment.getEndDate(), appointment.getStartDate(), appointment.getEndDate()));
  }

  private void isDateOccupied(
      LocalDateTime newStartDate,
      LocalDateTime newEndDate,
      LocalDateTime appointmentStartDate,
      LocalDateTime appointmentEndDate) {
    if (newStartDate.isBefore(appointmentEndDate) && newEndDate.isAfter(appointmentStartDate)) {
      throw new InternalServerError("Attempting to create an appointment in occupied dates");
    }
  }
}
