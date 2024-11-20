package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.ModifyAppointmentUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.appointment.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyAppointmentPort implements ModifyAppointmentUseCase {
  private final GenericRepository<Appointment> repository;

  @Override
  public Appointment modifyAppointment(Appointment newAppointment) {
    Appointment appointment =
        repository
            .findById(newAppointment.getAppointmentId())
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
    return repository.merge(newAppointment, appointment);
  }
}
