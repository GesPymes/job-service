package com.gespyme.infrastructure.adapters.job.output.repository.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.job.output.model.entity.JobEntity;
import com.gespyme.infrastructure.adapters.job.output.model.entity.QJobEntity;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IsPeriodicField implements QueryField<JobEntity> {

    private final PredicateBuilder<Boolean> predicateBuilder;

    @Override
    public String getFieldName() {
        return "periodic";
    }

    @Override
    public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
        booleanBuilder.and(
                predicateBuilder.getBooleanBuilder(
                        QJobEntity.jobEntity.periodic, searchCriteria));
    }
}
