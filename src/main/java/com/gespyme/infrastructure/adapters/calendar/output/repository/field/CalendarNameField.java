package com.gespyme.infrastructure.adapters.calendar.output.repository.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.CalendarEntity;
import com.gespyme.infrastructure.adapters.calendar.output.model.entity.QCalendarEntity;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarNameField implements QueryField<CalendarEntity> {

  private final PredicateBuilder<String> predicateBuilder;

  @Override
  public String getFieldName() {
    return "calendar_name";
  }

  @Override
  public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
    booleanBuilder.and(
        predicateBuilder.getBooleanBuilder(
            QCalendarEntity.calendarEntity.calendarName, searchCriteria));
  }
}
