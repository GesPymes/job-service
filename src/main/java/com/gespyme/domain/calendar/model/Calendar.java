package com.gespyme.domain.calendar.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Calendar {
  private String calendarId;
  private String calendarName;
  private List<UserByCalendar> users;
}
