package com.gespyme.application.calendar.port.output;

import com.gespyme.application.calendar.usecase.FindCalendarByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.calendar.model.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindCalendarByIdPort implements FindCalendarByIdUseCase {

  private final GenericRepository<Calendar> repository;

  public Calendar getCalendarById(String calendarId) {
    return repository
        .findById(calendarId)
        .orElseThrow(() -> new NotFoundException("Calendar not found"));
  }
}
