package com.gespyme.domain.job.model.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobFilter {
  private String employeeName;
  private Integer periodicity;
  private Boolean isPeriodic;
  private String customerName;
}
