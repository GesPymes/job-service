package com.gespyme.application.appointment.port.output;

import com.gespyme.application.appointment.usecase.FindAppointmentsUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.model.filter.AppointmentFilter;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.job.repository.JobRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAppointmentsPort implements FindAppointmentsUseCase {
  private final AppointmentRepository appointmentRepository;
  private final JobRepository jobRepository;
  private final List<FieldFilter<AppointmentFilter>> filters;

  @Override
  public List<Appointment> findAppointments(String jobId,
      AppointmentFilter appointmentFilter, boolean isPeriodicBatchCall) {
    jobRepository
            .findById(jobId)
            .orElseThrow(() -> new NotFoundException("Job not found"));
    appointmentFilter.setJobId(jobId);

    List<SearchCriteria> searchCriterias = new ArrayList<>();
    filters.stream()
        .filter(f -> f.apply(appointmentFilter))
        .forEach(f -> f.addSearchCriteria(appointmentFilter, searchCriterias));
    return appointmentRepository.findByCriteria(searchCriterias);
  }

}
