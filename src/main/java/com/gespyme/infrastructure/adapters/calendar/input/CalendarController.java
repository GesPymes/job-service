package com.gespyme.infrastructure.adapters.calendar.input;

import com.gespyme.application.calendar.port.input.ModifyCalendarPort;
import com.gespyme.application.calendar.usecase.CreateCalendarUseCase;
import com.gespyme.application.calendar.usecase.DeleteCalendarUseCase;
import com.gespyme.application.calendar.usecase.FindCalendarByIdUseCase;
import com.gespyme.application.calendar.usecase.FindCalendarsUseCase;
import com.gespyme.commons.model.job.CalendarBaseModelApi;
import com.gespyme.commons.model.job.CalendarFilterModelApi;
import com.gespyme.commons.model.job.CalendarModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.infrastructure.mapper.CalendarMapper;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
  private final CalendarMapper calendarMapper;
  private final FindCalendarsUseCase findCalendarsUseCase;
  private final FindCalendarByIdUseCase findCalendarByIdUseCase;
  private final DeleteCalendarUseCase deleteCalendarUseCase;
  private final CreateCalendarUseCase createCalendarUseCase;
  private final ModifyCalendarPort modifyCalendarUseCase;
  private final ValidatorService<CalendarBaseModelApi> validatorService;

  @GetMapping("/{calendarId}")
  public ResponseEntity<CalendarModelApi> getCalendarById(
      @PathVariable("calendarId") String calendarId) {
    validatorService.validateId(calendarId);
    Calendar calendar = findCalendarByIdUseCase.getCalendarById(calendarId);
    return ResponseEntity.ok(calendarMapper.map(calendar));
  }

  @GetMapping("/")
  public ResponseEntity<List<CalendarModelApi>> findCalendars(
      CalendarFilterModelApi calendarFilterModelApi) {
    validatorService.validate(calendarFilterModelApi, List.of(Validator.ONE_PARAM_NOT_NULL));
    CalendarFilter calendarFilter = calendarMapper.map(calendarFilterModelApi);
    List<Calendar> calendars = findCalendarsUseCase.findCalendars(calendarFilter);
    return ResponseEntity.ok(calendarMapper.map(calendars));
  }

  @DeleteMapping("/{calendarName}")
  public ResponseEntity<Void> deleteCalendar(@PathVariable("calendarName") String calendarName) {
    deleteCalendarUseCase.deleteCalendar(calendarName);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/")
  public ResponseEntity<CalendarModelApi> createCalendar(
      @RequestBody CalendarModelApi calendarApiModel) {
    validatorService.validate(calendarApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    Calendar calendar = createCalendarUseCase.createCalendar(calendarMapper.map(calendarApiModel));
    URI location = URI.create("/calendar/" + calendar.getCalendarId());
    return ResponseEntity.created(location).body(calendarMapper.map(calendar));
  }

  @PatchMapping("/{calendarName}")
  public ResponseEntity<CalendarModelApi> modifyCalendar(
      @PathVariable String calendarName, @RequestBody CalendarModelApi calendarApiModel) {
    validatorService.validate(calendarApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    Calendar calendar =
        modifyCalendarUseCase.modifyCalendar(calendarName, calendarMapper.map(calendarApiModel));
    return ResponseEntity.ok(calendarMapper.map(calendar));
  }
}
