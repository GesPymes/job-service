package com.gespyme.infrastructure.adapters.appointment.output.repository.jpa;

import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppointmentRepositorySpringJpa
    extends JpaRepository<AppointmentEntity, String>,
        QuerydslPredicateExecutor<AppointmentEntity> {}
