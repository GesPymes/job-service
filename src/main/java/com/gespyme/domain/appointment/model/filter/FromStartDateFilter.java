package com.gespyme.domain.appointment.model.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class FromStartDateFilter implements FieldFilter<AppointmentFilter> {
  @Override
  public boolean apply(AppointmentFilter filter) {
    return Objects.nonNull(filter.getStartDate());
  }

  @Override
  public void addSearchCriteria(AppointmentFilter filter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("start_date")
            .operation(SearchOperation.AFTER)
            .value(filter.getStartDate())
            .build());
  }
}
