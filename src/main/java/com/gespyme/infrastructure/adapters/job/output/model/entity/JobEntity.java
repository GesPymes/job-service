package com.gespyme.infrastructure.adapters.job.output.model.entity;

import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Column(name = "calendar_id")
  private String calendarId;

  @Column(name = "customer_id")
  private String customerId;

  @Column(name = "employee_id")
  private String employeeId;

  @Column(name = "periodicity")
  private Integer periodicity;

  @Column(name = "description")
  private String description;

  @OneToMany
  @JoinColumn(name = "job_id")
  private List<AppointmentEntity> appointments;
}
