package com.gespyme.application.appointment.port.output;

import com.gespyme.application.appointment.usecase.FindAppointmentsByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAppointmentsByIdPort implements FindAppointmentsByIdUseCase {

  private final AppointmentRepository repository;

  @Override
  public Appointment getAppointmentById(String appointmentId) {
    return repository
        .findById(appointmentId)
        .orElseThrow(() -> new NotFoundException("Calendar not found"));
  }
}
