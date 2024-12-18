package com.gespyme.application.calendar.port.input;

import com.gespyme.application.calendar.usecase.CreateCalendarUseCase;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.domain.calendar.repository.CalendarRepository;
import com.gespyme.domain.calendar.repository.CalendarService;
import com.gespyme.domain.calendar.repository.UserByCalendarRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCalendarPort implements CreateCalendarUseCase {

  @Value("${calendar.adminEmail}")
  private String adminEmail;

  private final CalendarRepository repository;
  private final CalendarService calendarService;
  private final UserByCalendarRepository userByCalendarRepository;

  public Calendar createCalendar(Calendar calendar) {
    String calendarId = calendarService.createCalendar(calendar.getCalendarName());
    calendarService.shareCalendar(calendarId, adminEmail);

    Calendar saved = repository.save(getCalendarToSave(calendar, calendarId));
    List<UserByCalendar> userByCalendarList = new ArrayList<>();

    addAdminToUsersByCalendar(calendar.getUsers()).forEach(
                        user -> {
                          calendarService.shareCalendar(calendarId, user.getUserEmail());
                          UserByCalendar userByCalendar =
                              userByCalendarRepository.save(
                                  user.toBuilder().calendarId(calendarId).build());
                          userByCalendarList.add(userByCalendar);
                        });
    return saved.toBuilder().users(userByCalendarList).build();
  }

  private Calendar getCalendarToSave(Calendar calendar, String calendarId) {
    return calendar.toBuilder().calendarId(calendarId).build();
  }

  private List<UserByCalendar> addAdminToUsersByCalendar(List<UserByCalendar> userByCalendarList) {
    userByCalendarList =
        Objects.isNull(userByCalendarList) ? new ArrayList<>() : userByCalendarList;
    userByCalendarList.add(UserByCalendar.builder().userEmail(adminEmail).build());
    return userByCalendarList;
  }
}
