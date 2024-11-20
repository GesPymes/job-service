package com.gespyme.domain.appointment.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Appointment {
  private String jobId;
  private String appointmentId;
  private String status;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  // private LocalDateTime nextEndDate;
  private Integer periodicity;
  private Boolean isPeriodic;
  private String description;
}
