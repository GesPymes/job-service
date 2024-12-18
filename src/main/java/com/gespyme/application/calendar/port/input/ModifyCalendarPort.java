package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.ModifyCalendarUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyCalendarPort implements ModifyCalendarUseCase {
  private final CalendarRepository repository;

  public Calendar modifyCalendar(String calendarId, Calendar newCalendarData) {
    Calendar calendar =
        repository
            .findById(calendarId)
            .orElseThrow(() -> new NotFoundException("Calendar not found"));
    return repository.merge(newCalendarData, calendar);
  }
}
