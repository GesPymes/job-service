package com.gespyme.domain.appointment.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder(toBuilder = true)
public class Appointment {
  private String jobId;
  private String calendarId;
  private String appointmentId;
  @With private String status;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private LocalDateTime nextEndDate;
}
