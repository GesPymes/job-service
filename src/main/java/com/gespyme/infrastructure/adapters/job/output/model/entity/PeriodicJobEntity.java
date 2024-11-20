package com.gespyme.infrastructure.adapters.job.output.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodicJobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String periodicJobId;

  @Column(name = "appointment_id")
  private String appointmentId;

  @Column(name = "status")
  private String status;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "next_end_date")
  private LocalDateTime nextEndDate;
}
