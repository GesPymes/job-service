package com.gespyme.domain.job.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder(toBuilder = true)
public class Job {
  private String jobId;
  private String customerId;
  private String employeeId;
  @With private Integer periodicity;
  @With private Boolean isPeriodic;
  private String description;
}
