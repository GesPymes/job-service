package com.gespyme.domain.appointment.model.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JobIdFilter implements FieldFilter<AppointmentFilter> {
  @Override
  public boolean apply(AppointmentFilter filter) {
    return true;
  }

  @Override
  public void addSearchCriteria(AppointmentFilter filter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("job_id")
            .operation(SearchOperation.EQUAL)
            .value(filter.getJobId())
            .build());
  }
}
