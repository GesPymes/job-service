package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.CalendarFilterModelApi;
import com.gespyme.commons.model.job.CalendarModelApi;
import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {

  CalendarFilter map(CalendarFilterModelApi calendarFilterModelApi);

  @Mapping(
      target = "users",
      expression = "java(calendar == null ? null : mapToEmail(calendar.getUsers()))")
  CalendarModelApi map(Calendar calendar);

  @Mapping(
      target = "users",
      expression =
          "java(calendarModelApi.getUsers() == null ? null : mapEmailsToUsers(calendarModelApi.getUsers()))")
  Calendar map(CalendarModelApi calendarModelApi);

  List<CalendarModelApi> map(List<Calendar> calendars);

  Calendar map(CalendarEntity calendarEntity);

  CalendarEntity mapToEntity(Calendar calendar);

  Calendar merge(Calendar newCalendar, @MappingTarget Calendar calendar);

  default List<UserByCalendar> mapEmailsToUsers(List<String> emails) {
    return emails.stream()
        .map(email -> UserByCalendar.builder().userEmail(email).build())
        .collect(Collectors.toList());
  }

  default List<String> mapToEmail(List<UserByCalendar> userByCalendars) {
    return userByCalendars.stream().map(UserByCalendar::getUserEmail).collect(Collectors.toList());
  }
}
