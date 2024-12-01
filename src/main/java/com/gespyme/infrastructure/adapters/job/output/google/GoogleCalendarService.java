package com.gespyme.infrastructure.adapters.job.output.google;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.domain.calendar.repository.CalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService implements CalendarService {

  @Value("${calendar.timezone}")
  private String ZONE_ID_MADRID;

  private final com.google.api.services.calendar.Calendar calendarService;

  public String createCalendar(String name) {
    Calendar calendar = new com.google.api.services.calendar.model.Calendar();
    calendar.setSummary(name);
    calendar.setTimeZone(ZONE_ID_MADRID);
    try {
      Calendar createdCalendar = calendarService.calendars().insert(calendar).execute();
      return createdCalendar.getId();
    } catch (IOException e) {
      throw new InternalServerError("Error creating calendar", e);
    }
  }

  @Override
  public void deleteCalendar(String calendarId) {
    try {
      calendarService.calendars().delete(calendarId).execute();
    } catch (IOException e) {
      throw new InternalServerError("Calendar cannot be deleted", e);
    }
  }

  @Override
  public Optional<String> createCalendarEvent(
      String description, String calendarId, LocalDateTime startDate, LocalDateTime endDate) {
    Event event =
        new Event().setSummary(description).setLocation(ZONE_ID_MADRID).setDescription(description);

    Date start = Date.from(startDate.atZone(ZoneId.of(ZONE_ID_MADRID)).toInstant());
    Date end = Date.from(endDate.atZone(ZoneId.of(ZONE_ID_MADRID)).toInstant());

    EventDateTime eventStartDateTime =
        new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(start));
    EventDateTime eventEndDateTime =
        new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(end));

    event.setStart(eventStartDateTime).setEnd(eventEndDateTime);

    Optional<String> eventId;
    try {
      eventId =
          Optional.ofNullable(calendarService.events().insert(calendarId, event).execute())
              .map(Event::getId);
    } catch (IOException e) {
      throw new InternalServerError("Event cannot be added to calendar", e);
    }
    return eventId;
  }

  @Override
  public void deleteCalendarEvent(String calendarId, String eventId) {
    try {
      calendarService.events().delete(calendarId, eventId).execute();
    } catch (IOException e) {
      throw new InternalServerError("Event cannot be deleted from calendar", e);
    }
  }

  @Override
  public void shareCalendar(String calendarId, String email) {
    AclRule rule = new AclRule();
    rule.setScope(new AclRule.Scope().setType("user").setValue(email));
    rule.setRole("reader");
    try {
      calendarService.acl().insert(calendarId, rule).execute();
    } catch (IOException e) {
      throw new InternalServerError("Calendar cannot be shared with email  " + email, e);
    }
  }
}
