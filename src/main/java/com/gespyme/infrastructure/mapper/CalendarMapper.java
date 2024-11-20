package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.CalendarFilterModelApi;
import com.gespyme.commons.model.job.CalendarModelApi;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.domain.filter.CalendarFilter;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {
  CalendarFilter map(CalendarFilterModelApi calendarFilterModelApi);

  Calendar map(CalendarModelApi calendarApiModel);

  List<CalendarModelApi> map(List<Calendar> calendars);

  CalendarModelApi map(Calendar calendars);

  Calendar map(CalendarEntity calendarApiModel);

  CalendarEntity mapToEntity(Calendar calendar);

  Calendar merge(Calendar newCalendar, @MappingTarget Calendar calendar);
}
