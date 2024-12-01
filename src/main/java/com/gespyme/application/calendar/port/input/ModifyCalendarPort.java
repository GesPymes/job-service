package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.ModifyCalendarUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
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
public class ModifyCalendarPort implements ModifyCalendarUseCase {
  private final CalendarRepository repository;
  private final List<FieldFilter<CalendarFilter>> calendarFilters;

  public Calendar modifyCalendar(String calendarName, Calendar newCalendarData) {
    CalendarFilter calendarFilter = new CalendarFilter();
    calendarFilter.setCalendarName(calendarName);
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    calendarFilters.stream()
        .filter(f -> f.apply(calendarFilter))
        .forEach(f -> f.addSearchCriteria(calendarFilter, searchCriterias));

    List<Calendar> calendars = repository.findByCriteria(searchCriterias);
    String calendarId =
        calendars.stream()
            .findFirst()
            .map(Calendar::getCalendarId)
            .orElseThrow(() -> new NotFoundException("Calendar name not found"));
    Calendar calendar =
        repository
            .findById(calendarId)
            .orElseThrow(() -> new NotFoundException("Calendar not found"));
    return repository.merge(newCalendarData, calendar);
  }
}
