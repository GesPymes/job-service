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

  @Column(name = "customer_id")
  private String customerId;

  @Column(name = "employee_id")
  private String employeeId;

  @Column(name = "description")
  private String description;

  @Column(name = "periodicity")
  private int periodicity;

  @Column(name = "is_periodic")
  private boolean periodic;

  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AppointmentEntity> appointments;
}
