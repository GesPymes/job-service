package com.gespyme.domain.calendar.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserByCalendar {
  private String userEmail;
  private String calendarId;
}
