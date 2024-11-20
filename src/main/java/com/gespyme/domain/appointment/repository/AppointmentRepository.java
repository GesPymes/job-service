package com.gespyme.domain.appointment.repository;

import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.appointment.model.Appointment;
import java.util.List;

public interface AppointmentRepository extends GenericRepository<Appointment> {
  List<Appointment> findByCriteria(List<SearchCriteria> searchCriteria);
}
