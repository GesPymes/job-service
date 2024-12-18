package com.gespyme.application.appointment.usecase;

import com.gespyme.domain.appointment.model.Appointment;

public interface ModifyAppointmentUseCase {
  Appointment modifyAppointment(String jobId, String appoitmentId, Appointment appointment);
}
