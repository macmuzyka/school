package com.school.repository.classschedule;

import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findTimeSlotByScheduleEntry(ScheduleEntry scheduleEntry);
}
