package com.gespyme.infrastructure.adapters.appointment.output.model.entity;

import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
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
@Table(name = "APPOINTMENT")
public class AppointmentEntity {
  @Id private String appointmentId;

  @Column(name = "job_id")
  private String jobId;

  @Column(name = "calendar_id")
  private String calendarId;

  @Column(name = "status")
  private String status;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @ManyToOne
  @JoinColumn(name = "job_id", insertable = false, updatable = false)
  private JobEntity job;
}
