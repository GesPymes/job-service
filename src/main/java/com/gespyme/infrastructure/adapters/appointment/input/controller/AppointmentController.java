package com.gespyme.infrastructure.adapters.appointment.input.controller;

import com.gespyme.application.appointment.usecase.*;
import com.gespyme.commons.model.job.*;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.appointment.model.Appointment;
import com.gespyme.domain.appointment.model.filter.AppointmentFilter;
import com.gespyme.infrastructure.mapper.AppointmentMapper;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {
  private final AppointmentMapper appointmentMapper;
  private final FindAppointmentsByIdUseCase findAppointmentsByIdUseCase;
  private final FindAppointmentsUseCase findAppointmentsUseCase;
  private final DeleteAppointmentUseCase deleteAppointmentUseCase;
  private final CreateAppointmentUseCase createAppointmentUseCase;
  private final ModifyAppointmentUseCase modifyAppointmentUseCase;
  private final ValidatorService<AppointmentBaseModelApi> validatorService;

  @GetMapping("/{appointmentId}")
  public ResponseEntity<AppointmentModelApi> getAppointmentById(
      @PathVariable("appointmentId") String appointmentId) {
    validatorService.validateId(appointmentId);
    Appointment appointment = findAppointmentsByIdUseCase.getAppointmentById(appointmentId);
    return ResponseEntity.ok(appointmentMapper.map(appointment));
  }

  @GetMapping("/")
  public ResponseEntity<List<AppointmentModelApi>> findAppointments(
      AppointmentFilterModelApi appointmentFilterModelApi) {
    validatorService.validate(appointmentFilterModelApi, List.of(Validator.ONE_PARAM_NOT_NULL));
    AppointmentFilter appointmentFilter = appointmentMapper.map(appointmentFilterModelApi);
    List<Appointment> appointments =
        findAppointmentsUseCase.findAppointments(appointmentFilter, false);
    return ResponseEntity.ok(appointmentMapper.map(appointments));
  }

  @DeleteMapping("/{appointmentId}")
  public ResponseEntity<Void> deleteAppointment(
      @PathVariable("appointmentId") String appointmentId) {
    validatorService.validateId(appointmentId);
    deleteAppointmentUseCase.deleteAppointment(appointmentId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/")
  public ResponseEntity<List<AppointmentModelApi>> createAppointment(
      @RequestBody AppointmentModelApi appointmentApiModel) {
    validatorService.validate(appointmentApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    List<Appointment> appointment =
        createAppointmentUseCase.createAppointment(appointmentMapper.map(appointmentApiModel));
    URI location = URI.create("/appointment/");
    return ResponseEntity.created(location).body(appointmentMapper.map(appointment));
  }

  @PatchMapping("/{appointmentId}")
  public ResponseEntity<AppointmentModelApi> modifyAppointment(
      @PathVariable("appointmentId") String appointmentId, @RequestBody AppointmentModelApi appointmentApiModel) {
    validatorService.validate(appointmentApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    Appointment appointments =
        modifyAppointmentUseCase.modifyAppointment(appointmentId, appointmentMapper.map(appointmentApiModel));
    return ResponseEntity.ok(appointmentMapper.map(appointments));
  }
}
