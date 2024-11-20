package com.gespyme.application.calendar.usecase;

import com.gespyme.domain.calendar.model.Calendar;

public interface FindCalendarByIdUseCase {
  Calendar getCalendarById(String calendarId);
}
