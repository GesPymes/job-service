package com.gespyme.infrastructure.adapters.calendar.output.repository.jpa;

import com.gespyme.infrastructure.adapters.calendar.output.model.entity.UserByCalendarEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersByCalendarRepositorySpringJpa
    extends JpaRepository<UserByCalendarEntity, String>,
        QuerydslPredicateExecutor<UserByCalendarEntity> {
    List<UserByCalendarEntity> findByUserEmail(String userId);
}
