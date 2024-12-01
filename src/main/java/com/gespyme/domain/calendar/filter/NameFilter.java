package com.gespyme.domain.calendar.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class NameFilter implements FieldFilter<CalendarFilter> {

  @Override
  public boolean apply(CalendarFilter calendarFilter) {
    return Objects.nonNull(calendarFilter.getCalendarName());
  }

  @Override
  public void addSearchCriteria(
      CalendarFilter calendarFilter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("calendar_name")
            .operation(SearchOperation.EQUAL)
            .value(calendarFilter.getCalendarName())
            .build());
  }
}
