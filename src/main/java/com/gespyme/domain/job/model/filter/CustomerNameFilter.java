package com.gespyme.domain.job.model.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CustomerNameFilter implements FieldFilter<JobFilter> {
  @Override
  public boolean apply(JobFilter filter) {
    return Objects.nonNull(filter.getCustomerName());
  }

  @Override
  public void addSearchCriteria(JobFilter filter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("customer_name")
            .operation(SearchOperation.LIKE)
            .value(filter.getCustomerName())
            .build());
  }
}
