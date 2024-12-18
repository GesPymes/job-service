package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.CalendarFilterModelApi;
import com.gespyme.commons.model.job.CalendarModelApi;
import com.gespyme.domain.calendar.filter.CalendarFilter;
import com.gespyme.domain.calendar.model.Calendar;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {

  CalendarFilter map(CalendarFilterModelApi calendarFilterModelApi);

  CalendarModelApi map(Calendar calendar);

  Calendar map(CalendarModelApi calendarModelApi);

  List<CalendarModelApi> map(List<Calendar> calendars);

  Calendar map(CalendarEntity calendarEntity);


  CalendarEntity mapToEntity(Calendar calendar);

  Calendar merge(Calendar newCalendar, @MappingTarget Calendar calendar);
}
