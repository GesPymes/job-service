package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.JobModelApi;
import com.gespyme.domain.job.model.Job;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {
  Job map(JobModelApi appointmentApiModel);

  List<JobModelApi> map(List<Job> appointments);

  JobModelApi map(Job appointments);

  Job map(JobEntity appointmentApiModel);

  JobEntity mapToEntity(Job appointment);

  Job merge(Job newJob, @MappingTarget Job appointment);
}
