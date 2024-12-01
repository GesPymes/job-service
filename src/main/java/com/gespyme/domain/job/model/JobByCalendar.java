package com.gespyme.domain.job.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobByCalendar {
  private String jobByCalendarId;
  private String jobId;
  private String calendarId;
}
