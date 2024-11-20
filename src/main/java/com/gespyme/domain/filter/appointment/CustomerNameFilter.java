package com.gespyme.domain.filter.appointment;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.filter.AppointmentFilter;
import com.gespyme.domain.filter.FieldFilter;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CustomerNameFilter implements FieldFilter<AppointmentFilter> {
  @Override
  public boolean apply(AppointmentFilter filter) {
    return Objects.nonNull(filter.getCustomerName());
  }

  @Override
  public void addSearchCriteria(
      AppointmentFilter filter, List<SearchCriteria> searchCriteriaList) {}
}
