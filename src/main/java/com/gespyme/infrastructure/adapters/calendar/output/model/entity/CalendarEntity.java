package com.gespyme.infrastructure.adapters.calendar.output.model.entity;

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
@Table(name = "CALENDAR")
public class CalendarEntity {
  @Id
  @Column(name = "calendar_id")
  private String calendarId;

  @Column(name = "calendar_name")
  private String calendarName;

  @Column(name = "user_by_calendar_id")
  private String userByCalendarId;

  @Column(name = "jobs_by_calendar_id")
  private String jobsByCalendarId;

  @OneToMany(mappedBy = "calendar", orphanRemoval = true)
  private List<UserByCalendarEntity> users;
}
