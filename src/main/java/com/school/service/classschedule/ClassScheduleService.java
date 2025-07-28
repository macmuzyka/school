package com.school.service.classschedule;

import com.school.model.dto.sclassschedule.DaySubject;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.repository.SchoolClassRepository;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.service.ClassRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ClassScheduleService {
    private final ClassScheduleRepository classScheduleRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ScheduleGeneratorService scheduleGeneratorService;
    private static final Logger log = LoggerFactory.getLogger(ClassScheduleService.class);

    public ClassScheduleService(ClassScheduleRepository classScheduleRepository, SchoolClassRepository schoolClassRepository, ScheduleGeneratorService scheduleGeneratorService) {
        this.classScheduleRepository = classScheduleRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.scheduleGeneratorService = scheduleGeneratorService;
    }

    public Map<String, List<DaySubject>> getClassScheduleGroupedByDaySubjectAndTimeframe(Long classId, boolean shouldExcludeBreaks) {
        Long scheduleIdForSchoolClass = findScheduleIdForClass(classId);
        ClassSchedule schedule = classScheduleRepository.findById(scheduleIdForSchoolClass)
                .orElseThrow(() -> new IllegalArgumentException("Could not find school class with id: " + classId));

        AtomicInteger classCounter = new AtomicInteger();
        return schedule.getScheduleEntries().stream()
                .flatMap(entry -> entry.getTimeSlots().stream()
                        .filter(slot -> !shouldExcludeBreaks || slot.isNotBreak())
                        .map(slot -> {
                            String key = slot.getDurationDisplay();
                            DaySubject ds = new DaySubject(slot.getId(),
                                    entry.getDayOfWeek(),
                                    slot.getSubject() == null ? "-----" : slot.getSubject().getName(),
                                    slot.getClassRoom() == null ? 0 : slot.getClassRoom().getRoomNumber()
                            );
                            classCounter.getAndIncrement();
                            return Map.entry(key, ds);
                        }))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        () -> new TreeMap<>(Comparator.comparing(key -> LocalTime.parse(key.split("-")[0]))),
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
                );
    }

    private Long findScheduleIdForClass(Long classId) {
        SchoolClass schoolClass = schoolClassRepository
                .findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find class with id" + classId));
        if (schoolClass.getClassSchedule() != null) {
            log.debug("Returning schedule for school class id: {}", schoolClass.getId());
            return schoolClass.getClassSchedule().getId();
        } else {
            log.warn("Attempting to generate empty schedule for school class id: {}", classId);
            return scheduleGeneratorService.generateSchedule(schoolClass).getId();
        }
    }
}
