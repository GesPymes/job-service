package com.gespyme.infrastructure.adapters.appointment.input.controller;

import com.gespyme.application.appointment.usecase.*;
import com.gespyme.commons.model.job.AppointmentFilterModelApi;
import com.gespyme.commons.model.job.AppointmentModelApi;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.filter.AppointmentFilter;
import com.gespyme.infrastructure.mapper.AppointmentMapper;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job/{jobId}/appointment")
public class AppointmentController {
  private final AppointmentMapper appointmentMapper;
  private final FindAppointmentsUseCase findAppointmentsUseCase;
  private final FindAppointmentsByIdUseCase findAppointmentsByIdUseCase;
  private final DeleteAppointmentUseCase deleteAppointmentUseCase;
  private final CreateAppointmentUseCase createAppointmentUseCase;
  private final ModifyAppointmentUseCase modifyAppointmentUseCase;

  @GetMapping("/{appointmentId}")
  public AppointmentModelApi getAppointmentById(@PathParam("appointmentId") String appointmentId) {
    Appointment appointment = findAppointmentsByIdUseCase.getAppointmentById(appointmentId);
    return appointmentMapper.map(appointment);
  }

  @GetMapping("/")
  public List<AppointmentModelApi> findAppointments(
      AppointmentFilterModelApi appointmentFilterModelApi) {
    AppointmentFilter appointmentFilter = appointmentMapper.map(appointmentFilterModelApi);
    List<Appointment> appointments = findAppointmentsUseCase.findAppointments(appointmentFilter);
    return appointmentMapper.map(appointments);
  }

  @DeleteMapping("/{appointmentId}")
  public void deleteAppointment(@PathParam("appointmentId") String appointmentId) {
    deleteAppointmentUseCase.deleteAppointment(appointmentId);
  }

  @PostMapping("/")
  public AppointmentModelApi createAppointment(AppointmentModelApi appointmentApiModel) {
    Appointment appointment =
        createAppointmentUseCase.createAppointment(appointmentMapper.map(appointmentApiModel));
    return appointmentMapper.map(appointment);
  }

  @PatchMapping("/")
  public AppointmentModelApi modifyAppointment(AppointmentModelApi appointmentApiModel) {
    Appointment appointment =
        modifyAppointmentUseCase.modifyAppointment(appointmentMapper.map(appointmentApiModel));
    return appointmentMapper.map(appointment);
  }
}
