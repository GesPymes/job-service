package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.JobFilterModelApi;
import com.gespyme.commons.model.job.JobModelApi;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.job.model.Job;
import com.gespyme.domain.job.model.filter.JobFilter;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {

  JobFilter map(JobFilterModelApi jobFilterModelApi);

  Job map(JobModelApi jobApiModel);

  List<JobModelApi> map(List<Job> jobs);

  JobModelApi map(Job job);

  @Mapping(target = "appointmentList", source = "appointments")
  Job map(JobEntity jobEntity);

  @Mapping(target = "appointments", source = "appointmentList")
  JobEntity mapToEntity(Job job);

  Job merge(Job newJob, @MappingTarget Job job);

  Appointment map (AppointmentEntity appointmentEntity);
}
