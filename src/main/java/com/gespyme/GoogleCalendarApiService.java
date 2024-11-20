package com.gespyme;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarApiService {

  private static final String APPLICATION_NAME = "GesPyme";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String SERVICE_ACCOUNT_KEY_FILE = "key.json";

  public static final String ZONE_ID_MADRID = "Europe/Madrid";

  /**
   * Retrieves the list of calendar events from the primary calendar.
   *
   * @return List of events retrieved.
   * @throws GeneralSecurityException If security credentials cannot be established.
   * @throws IOException If an error occurs while communicating with the Google Calendar API.
   */
  public static List<Event> getCalendarEvents() throws GeneralSecurityException, IOException {
    Calendar service = getCalendarService();

    // List the next 10 events from the primary calendar.
    DateTime now = new DateTime(System.currentTimeMillis());
    Events events =
        service
            .events()
            .list("gespyme.project@gmail.com")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
    List<Event> items = events.getItems();
    if (items.isEmpty()) {
      System.out.println("No upcoming events found.");
    } else {
      System.out.println("Upcoming events:");
      for (Event event : items) {
        DateTime start = event.getStart().getDateTime();
        if (start == null) {
          start = event.getStart().getDate();
        }
        System.out.printf("%s (%s)\n", event.getSummary(), start);
      }
    }
    return items;
  }

  public static Calendar getCalendarService() throws GeneralSecurityException, IOException {
    // Load the service account key JSON file

    GoogleCredentials credentials =
        GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE))
            .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

    // Build the service account credentials
    Calendar service =
        new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .build();
    /*

            com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
            calendar.setSummary("gesPyme");
            calendar.setTimeZone(ZONE_ID_MADRID);

            com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();

            System.out.println(createdCalendar.getId());

    */
    // Construct the Calendar service object
    return service;
  }

  public static void shareCalendar(Calendar service, String calendarId, String email)
      throws IOException {
    com.google.api.services.calendar.model.AclRule rule =
        new com.google.api.services.calendar.model.AclRule();
    rule.setScope(
        new com.google.api.services.calendar.model.AclRule.Scope().setType("user").setValue(email));
    rule.setRole("reader"); // O "owner" si quieres que tenga permisos de escritura

    // Insertar la regla de ACL para compartir el calendario
    service.acl().insert(calendarId, rule).execute();
    System.out.println("Calendario compartido con: " + email);
  }

  public static void main(String... args) throws IOException, GeneralSecurityException {
    Calendar service = getCalendarService();
    getCalendarEvents();
    shareCalendar(
        service,
        "b7a7dc6cf008f1a8bfccb860b50b95c6ba539fde5d153ab7f38f4882149348ef@group.calendar.google.com",
        "gespyme.project@gmail.com");
  }
}
