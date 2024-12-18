package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.DeleteCalendarUseCase;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCalendarPort implements DeleteCalendarUseCase {
  private final CalendarRepository repository;
  private final List<FieldFilter<CalendarFilter>> calendarFilters;
  private final CalendarService calendarService;

  public void deleteCalendar(String calendarName) {
    if (Objects.isNull(calendarName)) {
      return;
    }
    CalendarFilter calendarFilter = new CalendarFilter();
    calendarFilter.setCalendarName(calendarName);
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    calendarFilters.stream()
        .filter(f -> f.apply(calendarFilter))
        .forEach(f -> f.addSearchCriteria(calendarFilter, searchCriterias));

    List<Calendar> calendars = repository.findByCriteria(searchCriterias);
    calendars.stream()
        .map(Calendar::getCalendarId)
        .forEach(
            calendar -> {
              repository.deleteById(calendar);
              calendarService.deleteCalendar(calendar);
            });
  }

  public void deleteCalendarById(String calendarId) {
    if (Objects.isNull(calendarId)) {
      return;
    }
    repository.deleteById(calendarId);
    calendarService.deleteCalendar(calendarId);
  }
}
