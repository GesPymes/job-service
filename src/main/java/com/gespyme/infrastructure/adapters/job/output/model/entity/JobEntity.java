package com.gespyme.infrastructure.adapters.job.output.model.entity;

import com.gespyme.domain.job.model.JobByCalendar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "JOB")
public class JobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String jobId;

  @Column(name = "customer_id")
  private String customerId;

  @Column(name = "employee_id")
  private String employeeId;

  @Column(name = "description")
  private String description;
}
