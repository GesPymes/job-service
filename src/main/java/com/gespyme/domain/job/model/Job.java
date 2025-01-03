package com.gespyme.domain.job.model;

import com.gespyme.domain.appointment.model.Appointment;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder(toBuilder = true)
public class Job {
  private String jobId;
  @With private String customerId;
  @With private String employeeId;
  private String customerName;
  private String customerLastName;
  private String employeeName;
  @With private Integer periodicity;
  @With private boolean periodic;
  private String description;
  private List<Appointment> appointmentList;
}
