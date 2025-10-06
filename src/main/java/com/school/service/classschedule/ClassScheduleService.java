package com.school.service.classschedule;

import com.school.model.dto.sclassschedule.ClassScheduleSummary;
import com.school.model.dto.sclassschedule.DaySubject;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.repository.sql.SchoolClassRepository;
import com.school.repository.sql.classschedule.ClassRoomRepository;
import com.school.repository.sql.classschedule.ClassScheduleRepository;
import com.school.service.SchoolClassService;
import com.school.service.utils.ClassScheduleDSLKt;
import com.school.service.utils.EntityFetcher;
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
    private final SchoolClassService schoolClassService;
    private final ClassRoomRepository classRoomRepository;
    private final EmptyScheduleSchemaBuilderService emptyScheduleSchemaBuilderService;
    private static final Logger log = LoggerFactory.getLogger(ClassScheduleService.class);

    public ClassScheduleService(ClassScheduleRepository classScheduleRepository,
                                SchoolClassRepository schoolClassRepository,
                                SchoolClassService schoolClassService,
                                ClassRoomRepository classRoomRepository,
                                EmptyScheduleSchemaBuilderService emptyScheduleSchemaBuilderService) {
        this.classScheduleRepository = classScheduleRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.schoolClassService = schoolClassService;
        this.classRoomRepository = classRoomRepository;
        this.emptyScheduleSchemaBuilderService = emptyScheduleSchemaBuilderService;
    }

    //TODO: find a simpler and smoother way of achieving this
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
            return emptyScheduleSchemaBuilderService.generateEmptySchedule(schoolClass).getId();
        }
    }

    public ClassSchedule getClassScheduleById(Long scheduleId) {
        return EntityFetcher.getByIdOrThrow(classScheduleRepository::findById, scheduleId, "ClassSchedule");
    }

    public ClassSchedule updateClassSchedule(ClassSchedule classSchedule) {
        return classScheduleRepository.save(classSchedule);
    }

    public Long getClassScheduleIdByClassId(Long classId) {
        return schoolClassService.getSchoolClassById(classId).getClassSchedule().getId();
    }

    public ClassSchedule getClassScheduleByClassId(Long classId) {
        return schoolClassService.getSchoolClassById(classId).getClassSchedule();
    }

    //TODO: move to separate service
    public void purgeClassSchedule(ClassSchedule classSchedule) {
        classSchedule.getScheduleEntries()
                .forEach(se -> se.getTimeSlots()
                        .forEach(ts -> {
                                    ts.setSubject(null);
                                    if (ts.getClassRoom() != null) {
                                        ClassRoom classRoom = ts.getClassRoom();
                                        classRoom.detachFromTimeSlot();
                                        classRoomRepository.save(classRoom);
                                    }
                                    ts.setClassRoom(null);
                                }
                        )
                );
        classScheduleRepository.save(classSchedule);
    }

    public void purgeClassSchedule(Long classId) {
        ClassSchedule classSchedule = getClassScheduleByClassId(classId);
        purgeClassSchedule(classSchedule);
    }

    public List<ClassScheduleSummary> getSchedulesSummary() {
        return classScheduleRepository.findAll().stream().map(ClassScheduleDSLKt::asQuickView).toList();
    }
}
