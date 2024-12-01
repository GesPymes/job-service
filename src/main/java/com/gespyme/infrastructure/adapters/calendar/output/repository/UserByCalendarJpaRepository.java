package com.gespyme.infrastructure.adapters.calendar.output.repository;

import com.gespyme.domain.calendar.model.UserByCalendar;
import com.gespyme.domain.calendar.repository.UserByCalendarRepository;
import com.gespyme.infrastructure.adapters.calendar.output.repository.jpa.UsersByCalendarRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.UserByCalendarMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserByCalendarJpaRepository implements UserByCalendarRepository {

    private final UsersByCalendarRepositorySpringJpa usersByCalendarRepositorySpringJpa;
    private final UserByCalendarMapper mapper;

    @Override
    public List<UserByCalendar> getUserByCalendarByUserEmail(String userId) {
    return mapper.map(
        usersByCalendarRepositorySpringJpa
            .findByUserEmail(userId));
    }

    @Override
    public void save(UserByCalendar userByCalendar) {
        usersByCalendarRepositorySpringJpa.save(mapper.map(userByCalendar));
    }
}
