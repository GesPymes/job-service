package com.gespyme.application.appointment.usecase;

import com.gespyme.domain.appointment.model.Appointment;
import java.util.List;

public interface CreateAppointmentUseCase {
  List<Appointment> createAppointment(String jobId, Appointment calendar);
}
