package com.gespyme.application.calendar.usecase;

import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import java.util.List;

public interface FindCalendarsUseCase {
  List<Calendar> findCalendars(CalendarFilter calendarFilter);
}
