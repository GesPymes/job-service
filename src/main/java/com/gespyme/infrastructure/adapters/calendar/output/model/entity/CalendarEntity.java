package com.gespyme.infrastructure.adapters.calendar.output.model.entity;

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
public class CalendarEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String calendarId;

  @Column(name = "calendar_name")
  private String calendarName;
}
