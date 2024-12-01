package com.gespyme.application.calendar.usecase;

import com.gespyme.domain.calendar.model.Calendar;

public interface ModifyCalendarUseCase {
  Calendar modifyCalendar(String calendarId, Calendar calendar);
}
