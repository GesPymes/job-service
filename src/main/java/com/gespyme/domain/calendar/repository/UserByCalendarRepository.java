package com.gespyme.domain.calendar.repository;

import com.gespyme.domain.calendar.model.UserByCalendar;
import java.util.List;

public interface UserByCalendarRepository {
  List<UserByCalendar> getUserByCalendarByUserEmail(String userId);

  UserByCalendar save(UserByCalendar userByCalendar);
}
