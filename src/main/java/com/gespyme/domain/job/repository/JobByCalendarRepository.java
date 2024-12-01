package com.gespyme.domain.job.repository;

import com.gespyme.domain.job.model.JobByCalendar;
import java.util.List;

public interface JobByCalendarRepository {
    List<JobByCalendar> getCalendarsByJobId(String jobId);
    void delete(String jobByCalendarId);
    void save(JobByCalendar jobByCalendar);
}
