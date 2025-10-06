package com.school.repository.sql.classschedule;

import com.school.model.entity.classschedule.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> { }
