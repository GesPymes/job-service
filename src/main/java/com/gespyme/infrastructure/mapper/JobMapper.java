package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.JobFilterModelApi;
import com.gespyme.commons.model.job.JobModelApi;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.filter.JobFilter;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.PeriodicJobEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {

  JobFilter map(JobFilterModelApi jobFilterModelApi);

  Job map(JobModelApi jobApiModel);

  List<JobModelApi> map(List<Job> jobs);

  JobModelApi map(Job jobs);

  Job map(JobEntity jobEntity);

  JobEntity mapToEntity(Job job);

  Job merge(Job newJob, @MappingTarget Job job);

  Job map(PeriodicJobEntity periodicJobEntity);

  List<Job> mapEntityList(List<PeriodicJobEntity> periodicJobEntity);

  PeriodicJobEntity mapToPeriodicEntity(Job job);
}
