package com.gespyme.application.calendar.port.output;

import com.gespyme.application.calendar.usecase.FindCalendarsUseCase;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindCalendarsPort implements FindCalendarsUseCase {
  private final CalendarRepository calendarGenericRepository;
  private final List<FieldFilter<CalendarFilter>> calendarFilters;

  public List<Calendar> findCalendars(CalendarFilter calendarFilter) {
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    calendarFilters.stream()
        .filter(f -> f.apply(calendarFilter))
        .forEach(f -> f.addSearchCriteria(calendarFilter, searchCriterias));
    return calendarGenericRepository.findByCriteria(searchCriterias);
  }
}
