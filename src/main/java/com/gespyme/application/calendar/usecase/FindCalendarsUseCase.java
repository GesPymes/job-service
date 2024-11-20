package com.gespyme.application.calendar.usecase;

import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.filter.CalendarFilter;
import java.util.List;

public interface FindCalendarsUseCase {
  List<Calendar> findCalendars(CalendarFilter calendarFilter);
}
