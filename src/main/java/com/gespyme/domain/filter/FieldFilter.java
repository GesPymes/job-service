package com.gespyme.domain.filter;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import java.util.List;

public interface FieldFilter<T> {

  boolean apply(T filter);

  void addSearchCriteria(T filter, List<SearchCriteria> searchCriteriaList);
}
