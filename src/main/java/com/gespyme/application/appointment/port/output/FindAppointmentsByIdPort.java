package com.gespyme.application.appointment.port.output;

import com.gespyme.application.appointment.usecase.FindAppointmentsByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAppointmentsByIdPort implements FindAppointmentsByIdUseCase {

  private final AppointmentRepository repository;
  private final JobRepository jobRepository;

  @Override
  public Appointment getAppointmentById(String jobId, String appointmentId) {
    jobRepository
            .findById(jobId)
            .orElseThrow(() -> new NotFoundException("Job not found"));
    return repository
        .findById(appointmentId)
        .orElseThrow(() -> new NotFoundException("Calendar not found"));
  }
}
