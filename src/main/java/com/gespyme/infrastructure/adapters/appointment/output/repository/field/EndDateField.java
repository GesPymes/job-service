package com.gespyme.infrastructure.adapters.appointment.output.repository.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.AppointmentEntity;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.QAppointmentEntity;
import com.querydsl.core.BooleanBuilder;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EndDateField implements QueryField<AppointmentEntity> {

  private final PredicateBuilder<LocalDateTime> predicateBuilder;

  @Override
  public String getFieldName() {
    return "end_date";
  }

  @Override
  public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
    booleanBuilder.and(
        predicateBuilder.getBooleanBuilder(
            QAppointmentEntity.appointmentEntity.endDate, searchCriteria));
  }
}
