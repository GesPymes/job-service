package com.gespyme.domain.calendar.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Calendar {
  private String calendarId;
  private String calendarName;
}
