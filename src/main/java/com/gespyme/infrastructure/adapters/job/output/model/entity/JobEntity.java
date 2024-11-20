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
public class JobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String jobId;

  @Column(name = "calendar_id")
  private String calendarId;

  @Column(name = "employee_id")
  private String employeeId;

  @Column(name = "periodicity")
  private Integer periodicity;

  @Column(name = "is_periodic")
  private boolean isPeriodic;

  @Column(name = "description")
  private String descripton;
}
