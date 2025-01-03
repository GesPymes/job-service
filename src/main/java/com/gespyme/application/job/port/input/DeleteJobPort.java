package com.gespyme.application.job.port.input;

import com.gespyme.application.appointment.port.input.DeleteAppointmentPort;
import com.gespyme.application.job.usecase.DeleteJobUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.job.AppointmentStatus;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteJobPort implements DeleteJobUseCase {

  private final JobRepository repository;
  private final AppointmentRepository appointmentRepository;
  private final DeleteAppointmentPort deleteAppointmentPort;

  @Override
  public void deleteJob(String jobId) {
    Job job = repository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
    List<Appointment> appointments =
        appointmentRepository.findByCriteria(
            List.of(
                SearchCriteria.builder()
                    .key("jobId")
                    .value(jobId)
                    .operation(SearchOperation.EQUAL)
                    .build()));
    appointments.stream()
        .filter(
            appointment ->
                AppointmentStatus.PENDING.equals(
                    AppointmentStatus.valueOf(appointment.getStatus())))
        .forEach(
            appointment ->
                    deleteAppointmentPort.deleteAppointment(jobId, appointment.getAppointmentId()));
    repository.deleteById(job.getJobId());
  }
}
