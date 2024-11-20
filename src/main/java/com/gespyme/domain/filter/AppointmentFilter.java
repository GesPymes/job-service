package com.gespyme.domain.filter;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentFilter {
  private String status;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String employeeName;
  private Integer periodicity;
  private Boolean isPeriodic;
  private String customerName;
}
