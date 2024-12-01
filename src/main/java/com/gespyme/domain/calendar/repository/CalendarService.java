package com.gespyme.domain.calendar.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarService {

  String createCalendar(String calendarName);

  void deleteCalendar(String calendarName);

  Optional<String> createCalendarEvent(
      String description, String calendarId, LocalDateTime startDate, LocalDateTime endDate);

  void deleteCalendarEvent(String calendarId, String eventId);

  void shareCalendar(String calendarId, String userEmail);
}
