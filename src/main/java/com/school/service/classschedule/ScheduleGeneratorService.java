package com.school.service.classschedule;

import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.SchoolClassRepository;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.repository.classschedule.ScheduleEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class ScheduleGeneratorService {
    private final SchoolClassRepository schoolClassRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final ScheduleEntryRepository scheduleEntryRepository;
    private final TimeSlotService timeSlotService;
    private static final Logger log = LoggerFactory.getLogger(ScheduleGeneratorService.class);

    public ScheduleGeneratorService(SchoolClassRepository schoolClassRepository,
                                    ClassScheduleRepository classScheduleRepository,
                                    ScheduleEntryRepository scheduleEntryRepository,
                                    TimeSlotService timeSlotService) {
        this.schoolClassRepository = schoolClassRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.scheduleEntryRepository = scheduleEntryRepository;
        this.timeSlotService = timeSlotService;
    }

    public ClassSchedule generateSchedule(SchoolClass schoolClass) {
        if (schoolClass.getClassSchedule() != null) {
            return schoolClass.getClassSchedule();
        } else {
            ClassSchedule classSchedule = classScheduleRepository
                    .save(new ClassSchedule(schoolClass.className() + " schedule", new ArrayList<>(), schoolClass));

            List<ScheduleEntry> entries = new LinkedList<>();
            //TODO: verify correctness, when saving just objects, and then all entities at once (collections via constructor)
            for (DayOfWeek day : EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)) {
                ScheduleEntry dayEntry = scheduleEntryRepository.save(new ScheduleEntry(classSchedule, day, new ArrayList<>()));
                List<TimeSlot> timeSlots = dayEntry.getTimeSlots();
                timeSlots.addAll(timeSlotService.buildTimeSlots(dayEntry));

                scheduleEntryRepository.save(dayEntry);
                entries.add(dayEntry);
            }

            List<ScheduleEntry> scheduleEntries = classSchedule.getScheduleEntries();
            scheduleEntries.addAll(entries);
            ClassSchedule generatedSchedule = classScheduleRepository.save(classSchedule);
            schoolClass.setClassSchedule(generatedSchedule);
            schoolClassRepository.save(schoolClass);
            return generatedSchedule;
        }
    }
}