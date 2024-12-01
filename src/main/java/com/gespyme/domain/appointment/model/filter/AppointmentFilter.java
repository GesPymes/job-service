package com.gespyme.domain.appointment.model.filter;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentFilter {
  private String status;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
