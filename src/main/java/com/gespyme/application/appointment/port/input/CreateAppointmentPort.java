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
        jobRepository
            .findById(jobId)
            .orElseThrow(() -> new NotFoundException("Job not found"));
    List<JobByCalendar> jobByCalendars =
        jobByCalendarRepository.getCalendarsByJobId(jobId);

    List<Appointment> appointments = new ArrayList<>();

    jobByCalendars.stream()
        .forEach(
            calendar -> {
              Optional<String> id =
                  calendarService.createCalendarEvent(
                      job.getDescription(),
                      calendar.getCalendarId(),
                      appointment.getStartDate(),
                      appointment.getEndDate());
              Appointment savedAppointment =
                  repository.save(
                      appointment.toBuilder()
                          .calendarId(calendar.getCalendarId())
                          .appointmentId(
                              id.orElseThrow(
                                  () -> new InternalServerError("Exception saving event")))
                          .build());
              appointments.add(savedAppointment);
            });
    return appointments;
  }
}
