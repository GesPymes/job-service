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
@Table(name = "JOBS_BY_CALENDAR")
public class JobByCalendarEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "jobs_by_calendar_id")
  private String jobsByCalendarId;

  @Column(name = "calendar_id")
  private String calendarId;

  @Column(name = "job_id")
  private String jobId;

}
