package com.gespyme.infrastructure.adapters.calendar.output.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_BY_CALENDAR")
public class UserByCalendarEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "users_by_calendar_id", updatable = false, nullable = false)
  private String userByCalendarId;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "calendar_id")
  private String calendarId;

  @ManyToOne
  @JoinColumn(name = "calendar_id", insertable = false, updatable = false)
  private CalendarEntity calendar;
}
