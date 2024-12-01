package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.ModifyAppointmentUseCase;
import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.domain.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyAppointmentPort implements ModifyAppointmentUseCase {
  private final AppointmentRepository repository;
  private final JobRepository jobRepository;
  private final JobByCalendarRepository jobByCalendarRepository;
  private final CalendarService calendarService;

  @Override
  public Appointment modifyAppointment(String appointmentId, Appointment newAppointment) {
    Appointment appointment =
        repository
            .findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
    Job job =
        jobRepository
            .findById(appointment.getJobId())
            .orElseThrow(() -> new NotFoundException("Job not found"));
    jobByCalendarRepository.getCalendarsByJobId(job.getJobId()).stream()
        .map(JobByCalendar::getCalendarId)
        .forEach(
            calendar ->
                calendarService.deleteCalendarEvent(calendar, appointment.getAppointmentId()));

    String id =
        calendarService
            .createCalendarEvent(
                    job.getDescription(),
                appointment.getCalendarId(), appointment.getStartDate(), appointment.getEndDate())
            .orElseThrow(() -> new InternalServerError("Cannot create event"));

    return repository.merge(newAppointment.toBuilder().appointmentId(id).build(), appointment);
  }
}
