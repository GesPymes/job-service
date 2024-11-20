package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.CreateCalendarUseCase;
import com.gespyme.commons.repository.GenericRepository;
import com.gespyme.domain.calendar.model.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCalendarPort implements CreateCalendarUseCase {
  private final GenericRepository<Calendar> repository;

  public Calendar createCalendar(Calendar calendar) {
    return repository.save(calendar);
  }
}
