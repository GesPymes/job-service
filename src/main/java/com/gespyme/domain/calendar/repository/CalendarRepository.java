package com.gespyme.domain.calendar.repository;

import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.calendar.model.Calendar;
import java.util.List;

public interface CalendarRepository extends GenericRepository<Calendar> {
  List<Calendar> findByCriteria(List<SearchCriteria> searchCriteria);
}
