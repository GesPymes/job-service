package com.gespyme.domain.job.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Job {
  private String jobId;
  private String calendarId;
  private String employeeId;
  private Integer periodicity;
  private boolean isPeriodic;
  private String description;
}
