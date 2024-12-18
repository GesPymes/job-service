package com.gespyme.infrastructure.adapters.appointment.output.repository.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.appointment.output.model.entity.QAppointmentEntity;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobIdField implements QueryField {

  private final PredicateBuilder<String> predicateBuilder;

  @Override
  public String getFieldName() {
    return "job_id";
  }

  @Override
  public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
    booleanBuilder.and(
        predicateBuilder.getBooleanBuilder(
            QAppointmentEntity.appointmentEntity.jobId, searchCriteria));
  }
}
