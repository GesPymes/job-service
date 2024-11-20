package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.CreateAppointmentUseCase;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.appointment.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAppointmentPort implements CreateAppointmentUseCase {
  private final GenericRepository<Appointment> repository;

  @Override
  public Appointment createAppointment(Appointment appointment) {
    return repository.save(appointment);
  }
}
