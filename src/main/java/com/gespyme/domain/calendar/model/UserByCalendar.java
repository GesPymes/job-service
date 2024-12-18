package com.gespyme.domain.calendar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserByCalendar {
  private String userByCalendarId;
  private String userEmail;
  private String calendarId;
}
