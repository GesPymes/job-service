package com.gespyme.application.appointment.port.input;

import com.gespyme.application.appointment.usecase.DeleteAppointmentUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.appointment.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAppointmentPort implements DeleteAppointmentUseCase {
  private final GenericRepository<Appointment> repository;

  @Override
  public void deleteAppointment(String appointmentId) {
    Appointment appointment =
        repository
            .findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment not found"));
    repository.deleteById(appointment.getAppointmentId());
  }
}
