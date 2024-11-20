package com.gespyme.application.appointment.port.output;

import com.gespyme.application.appointment.usecase.FindAppointmentsUseCase;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.repository.AppointmentRepository;
import com.gespyme.domain.filter.AppointmentFilter;
import com.gespyme.domain.filter.FieldFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAppointmentsPort implements FindAppointmentsUseCase {
  private final AppointmentRepository appointmentRepository;
  private final List<FieldFilter<AppointmentFilter>> filters;

  @Override
  public List<Appointment> findAppointments(AppointmentFilter appointmentFilter) {
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    filters.stream()
        .filter(f -> f.apply(appointmentFilter))
        .forEach(f -> f.addSearchCriteria(appointmentFilter, searchCriterias));
    return appointmentRepository.findByCriteria(searchCriterias);
  }
}
