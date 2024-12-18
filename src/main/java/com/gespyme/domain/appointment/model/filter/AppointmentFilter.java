package com.gespyme.domain.appointment.model.filter;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentFilter {
  private String status;
  private String jobId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private List<LocalDateTime> endDates;
}
