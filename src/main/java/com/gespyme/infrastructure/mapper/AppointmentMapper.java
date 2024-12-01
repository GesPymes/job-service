package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.job.AppointmentFilterModelApi;
import com.gespyme.commons.model.job.AppointmentModelApi;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.model.filter.AppointmentFilter;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
  AppointmentFilter map(AppointmentFilterModelApi appointmentApiModel);

  Appointment map(AppointmentModelApi appointmentApiModel);

  List<AppointmentModelApi> map(List<Appointment> appointments);

  AppointmentModelApi map(Appointment appointments);

  Appointment map(AppointmentEntity appointmentApiModel);

  AppointmentEntity mapToEntity(Appointment appointment);

  Appointment merge(Appointment newAppointment, @MappingTarget Appointment appointment);
}
