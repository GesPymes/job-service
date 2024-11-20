package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.DeleteCalendarUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.calendar.model.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCalendarPort implements DeleteCalendarUseCase {
  private final GenericRepository<Calendar> repository;

  public void deleteCalendar(String calendarId) {
    Calendar calendar =
        repository
            .findById(calendarId)
            .orElseThrow(() -> new NotFoundException("Calendar not found"));
    repository.deleteById(calendar.getCalendarId());
  }
}
