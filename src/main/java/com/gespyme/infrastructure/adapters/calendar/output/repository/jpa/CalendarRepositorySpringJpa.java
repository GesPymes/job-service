package com.gespyme.infrastructure.adapters.calendar.output.repository.jpa;

import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepositorySpringJpa
    extends JpaRepository<CalendarEntity, String>, QuerydslPredicateExecutor<CalendarEntity> {}
