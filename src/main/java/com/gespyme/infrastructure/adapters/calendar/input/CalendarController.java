package com.gespyme.infrastructure.adapters.calendar.input;

import com.gespyme.application.calendar.port.input.ModifyCalendarUseCase;
import com.gespyme.application.calendar.usecase.CreateCalendarUseCase;
import com.gespyme.application.calendar.usecase.DeleteCalendarUseCase;
import com.gespyme.application.calendar.usecase.FindCalendarByIdUseCase;
import com.gespyme.application.calendar.usecase.FindCalendarsUseCase;
import com.gespyme.commons.model.job.CalendarBaseModelApi;
import com.gespyme.commons.model.job.CalendarFilterModelApi;
import com.gespyme.commons.model.job.CalendarModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.filter.CalendarFilter;
import com.gespyme.infrastructure.mapper.CalendarMapper;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  private final ModifyCalendarUseCase modifyCalendarUseCase;
  private final ValidatorService<CalendarBaseModelApi> validatorService;

  @GetMapping("/{calendarId}")
  public CalendarModelApi getCalendarById(@PathParam("calendarId") String calendarId) {
    validatorService.validateId(calendarId);
    Calendar calendar = findCalendarByIdUseCase.getCalendarById(calendarId);
    return calendarMapper.map(calendar);
  }

  @GetMapping("/")
  public List<CalendarModelApi> findCalendars(CalendarFilterModelApi calendarFilterModelApi) {
    validatorService.validate(calendarFilterModelApi, List.of(Validator.ONE_PARAM_NOT_NULL));
    CalendarFilter calendarFilter = calendarMapper.map(calendarFilterModelApi);
    List<Calendar> calendars = findCalendarsUseCase.findCalendars(calendarFilter);
    return calendarMapper.map(calendars);
  }

  @DeleteMapping("/{calendarId}")
  public void deleteCalendar(@PathParam("calendarId") String calendarId) {
    validatorService.validateId(calendarId);
    deleteCalendarUseCase.deleteCalendar(calendarId);
  }

  @PostMapping("/")
  public CalendarModelApi createCalendar(CalendarModelApi calendarApiModel) {
    validatorService.validate(calendarApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    Calendar calendar = createCalendarUseCase.createCalendar(calendarMapper.map(calendarApiModel));
    return calendarMapper.map(calendar);
  }

  @PatchMapping("/")
  public CalendarModelApi modifyCalendar(CalendarModelApi calendarApiModel) {
    validatorService.validate(calendarApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    Calendar calendar = modifyCalendarUseCase.modifyCalendar(calendarMapper.map(calendarApiModel));
    return calendarMapper.map(calendar);
  }
}
