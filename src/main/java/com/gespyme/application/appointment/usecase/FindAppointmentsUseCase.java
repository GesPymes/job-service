package com.gespyme.application.appointment.usecase;

import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.model.filter.AppointmentFilter;
import java.util.List;

public interface FindAppointmentsUseCase {
  List<Appointment> findAppointments(
          String jobId, AppointmentFilter appointmentFilter, boolean isPeriodicBatchCall);
}
