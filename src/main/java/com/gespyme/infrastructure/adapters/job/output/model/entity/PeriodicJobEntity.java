package com.gespyme.infrastructure.adapters.job.output.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERIODIC_JOB")
public class PeriodicJobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String jobId;

  @Column(name = "employee_id")
  private String employeeId;

  @Column(name = "customer_id")
  private String customerId;

  @Column(name = "periodicity")
  private Integer periodicity;

  @Column(name = "is_periodic")
  private Boolean isPeriodic;

  @Column(name = "description")
  private String description;
}
