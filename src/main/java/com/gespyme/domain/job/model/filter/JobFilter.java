package com.gespyme.domain.job.model.filter;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobFilter {
  private String jobId;
  private String employeeName;
  private Integer periodicity;
  private Boolean periodic;
  private String customerName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
