package com.gespyme.infrastructure.adapters.job.output.repository;

import com.gespyme.domain.job.model.JobByCalendar;
import com.gespyme.domain.job.repository.JobByCalendarRepository;
import com.gespyme.infrastructure.adapters.job.output.repository.jpa.JobsByCalendarRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.JobByCalendarMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JobByCalendarRepositoryJpa implements JobByCalendarRepository {

  private final JobsByCalendarRepositorySpringJpa jobsByCalendarRepositorySpringJpa;
  private final JobByCalendarMapper mapper;

  @Override
  public List<JobByCalendar> getCalendarsByJobId(String jobId) {
    return mapper.map(jobsByCalendarRepositorySpringJpa.findByJobId(jobId));
  }

  @Override
  public void delete(String jobByCalendarId) {
    jobsByCalendarRepositorySpringJpa.deleteById(jobByCalendarId);
  }

  @Override
  public void save(JobByCalendar jobByCalendar) {
    jobsByCalendarRepositorySpringJpa.save(mapper.map(jobByCalendar));
  }
}
