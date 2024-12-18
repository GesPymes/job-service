package com.gespyme.domain.calendar.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Calendar {
  private String calendarId;
  private String calendarName;
  private List<UserByCalendar> users;

  public void addUser(UserByCalendar user) {
    if(users == null || users.isEmpty()) {
      users = new ArrayList<>();
    }
    this.users.add(user);
  }
}
