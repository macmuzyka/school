package com.school.service.classschedule;

import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.sql.SchoolClassRepository;
import com.school.repository.sql.classschedule.ClassScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class EmptyScheduleSchemaBuilderService {
    private static final Logger log = LoggerFactory.getLogger(EmptyScheduleSchemaBuilderService.class);
    private final SchoolClassRepository schoolClassRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final TimeSlotBuilderService timeSlotBuilderService;

    public EmptyScheduleSchemaBuilderService(SchoolClassRepository schoolClassRepository,
                                             ClassScheduleRepository classScheduleRepository,
                                             TimeSlotBuilderService timeSlotBuilderService) {
        this.schoolClassRepository = schoolClassRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.timeSlotBuilderService = timeSlotBuilderService;
    }

    public ClassSchedule generateEmptySchedule(SchoolClass schoolClass) {
        log.debug("Generating schedule for class: {}", schoolClass.getName());
        if (schoolClass.getClassSchedule() != null) {
            schoolClass.setClassSchedule(null);
            schoolClassRepository.save(schoolClass);
        }
        ClassSchedule classSchedule = new ClassSchedule(schoolClass.className() + " schedule", new ArrayList<>(), schoolClass);
        schoolClass.setClassSchedule(classSchedule);

        List<ScheduleEntry> entries = new LinkedList<>();
        for (DayOfWeek day : EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)) {
            ScheduleEntry dayEntry = new ScheduleEntry(classSchedule, day, new ArrayList<>());
            List<TimeSlot> timeSlots = dayEntry.getTimeSlots();
            timeSlots.addAll(timeSlotBuilderService.buildTimeSlots(dayEntry));
            entries.add(dayEntry);
        }

        List<ScheduleEntry> scheduleEntries = classSchedule.getScheduleEntries();
        scheduleEntries.addAll(entries);
        schoolClass.setClassSchedule(classSchedule);
        schoolClassRepository.save(schoolClass);
        return classScheduleRepository.save(classSchedule);
    }
}