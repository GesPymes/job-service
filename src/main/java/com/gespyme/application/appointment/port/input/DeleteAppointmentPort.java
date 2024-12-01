package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.DeleteAppointmentUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAppointmentPort implements DeleteAppointmentUseCase {
  private final AppointmentRepository repository;
  private final CalendarService calendarService;

  @Override
  public void deleteAppointment(String appointmentId) {
    Appointment appointment =
        repository
            .findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
    calendarService.deleteCalendarEvent(appointment.getCalendarId(), appointmentId);
    repository.deleteById(appointment.getAppointmentId());
  }
}
