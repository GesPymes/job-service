package com.gespyme.application.appointment.usecase;

import com.gespyme.domain.appointment.model.Appointment;

public interface FindAppointmentsByIdUseCase {
  Appointment getAppointmentById(String appointmentId);
}
