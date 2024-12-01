package com.gespyme.infrastructure.mapper;

import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobByCalendarEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobByCalendarMapper {
  JobByCalendar map(JobByCalendarEntity entity);

  List<JobByCalendar> map(List<JobByCalendarEntity> entity);

  JobByCalendarEntity map(JobByCalendar entity);
}
