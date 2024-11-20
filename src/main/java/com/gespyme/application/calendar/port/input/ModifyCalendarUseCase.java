package com.gespyme.application.calendar.port.input;

import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.calendar.model.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyCalendarUseCase {
  private final GenericRepository<Calendar> repository;

  public Calendar modifyCalendar(Calendar newCalendarData) {
    Calendar calendar =
        repository
            .findById(newCalendarData.getCalendarId())
            .orElseThrow(() -> new NotFoundException("Calendar not found"));
    return repository.merge(newCalendarData, calendar);
  }
}
