package com.gespyme.infrastructure.mapper;

import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.UserByCalendarEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserByCalendarMapper {
    UserByCalendar map(UserByCalendarEntity entity);
    List<UserByCalendar> map(List<UserByCalendarEntity> entity);
    UserByCalendarEntity map(UserByCalendar entity);
}
