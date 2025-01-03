package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.ModifyAppointmentUseCase;
import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.job.AppointmentStatus;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.Objects;
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
  public Appointment modifyAppointment(String jobId, String appointmentId, Appointment newAppointment) {
    Appointment appointment =
        repository
            .findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
    Job job =
        jobRepository
            .findById(jobId)
            .orElseThrow(() -> new NotFoundException("Job not found"));

    if (shouldDeleteEvent(newAppointment, appointment)) {
      deleteFromCalendar(appointment);
      appointmentId =
              calendarService
                      .createCalendarEvent(
                              job.getDescription(),
                              appointment.getCalendarId(),
                              appointment.getStartDate(),
                              appointment.getEndDate())
                      .orElseThrow(() -> new InternalServerError("Cannot create event"));
    }



    return repository.merge(newAppointment.toBuilder().appointmentId(appointmentId).build(), appointment);
  }

  private boolean shouldDeleteEvent(Appointment newAppointment, Appointment oldAppointment) {
    return AppointmentStatus.CANCELLED.toString().equals(newAppointment.getStatus())
        || isDateChanged(newAppointment, oldAppointment);
  }

  private boolean isDateChanged(Appointment newAppointment, Appointment oldAppointment) {
    return isStartDateChanged(newAppointment, oldAppointment) || isEndDateChanged(newAppointment, oldAppointment);
  }

  private boolean isStartDateChanged(Appointment newAppointment, Appointment oldAppointment) {
    return Objects.nonNull(newAppointment.getStartDate()) && !newAppointment.getStartDate().equals(oldAppointment.getStartDate());
  }

  private boolean isEndDateChanged(Appointment newAppointment, Appointment oldAppointment) {
    return Objects.nonNull(newAppointment.getEndDate()) && !newAppointment.getEndDate().equals(oldAppointment.getEndDate());
  }

  private void deleteFromCalendar(Appointment appointment) {
    calendarService.deleteCalendarEvent(appointment.getCalendarId(), appointment.getAppointmentId());
  }
}
