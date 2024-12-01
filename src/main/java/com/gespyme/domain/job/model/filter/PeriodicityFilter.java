package com.gespyme.domain.job.model.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PeriodicityFilter implements FieldFilter<JobFilter> {
  @Override
  public boolean apply(JobFilter filter) {
    return Objects.nonNull(filter.getIsPeriodic());
  }

  @Override
  public void addSearchCriteria(JobFilter filter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("periodicity")
            .operation(SearchOperation.EQUAL)
            .value(filter.getIsPeriodic())
            .build());
  }
}
