package com.school.service.classschedule;

import com.school.configuration.ApplicationConfig;
import com.school.model.dto.sclassschedule.ClassScheduleDisplayDTO;
import com.school.model.dto.sclassschedule.LessonUnitDTO;
import com.school.model.dto.sclassschedule.ScheduleEntryDTO;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.SchoolClassRepository;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.repository.classschedule.ScheduleEntryRepository;
import com.school.repository.classschedule.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassScheduleService {
    private final ClassScheduleRepository classScheduleRepository;
    private final ScheduleEntryRepository scheduleEntryRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ApplicationConfig applicationConfig;

    public ClassScheduleService(ClassScheduleRepository classScheduleRepository,
                                ScheduleEntryRepository scheduleEntryRepository,
                                TimeSlotRepository timeSlotRepository,
                                SchoolClassRepository schoolClassRepository, final ApplicationConfig applicationConfig
    ) {
        this.classScheduleRepository = classScheduleRepository;
        this.scheduleEntryRepository = scheduleEntryRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.applicationConfig = applicationConfig;
    }

    public Map<String, ClassScheduleDisplayDTO> classScheduleDisplayDTO() {
        List<ClassSchedule> schedules = classScheduleRepository.findAll();
        return schedules.stream()
                .collect(Collectors.toMap(
                        ClassSchedule::getClassScheduleName,
                        schedule -> new ClassScheduleDisplayDTO(schedule.getScheduleEntries().stream()
                                .map(entry -> new ScheduleEntryDTO(entry.getDayOfWeek(), entry.getTimeSlots().stream()
                                        .map(slot -> new LessonUnitDTO(
                                                slot.getStartTime().toString() + "-" + slot.getEndTime(),
                                                slot.getSubject() == null ? "No assigned subject yet!" : slot.getSubject().getName()
                                        )).toList()
                                )).toList()
                        )
                ));
    }

    public ClassSchedule generateEmptyScheduleForSchoolClass(Long schoolClassId) {
        SchoolClass schoolClassFound;
        if (schoolClassId == null) {
            schoolClassFound = schoolClassRepository
                    .findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Could not find any class to generate class schedule to!"));
        } else {
            schoolClassFound = schoolClassRepository
                    .findById(schoolClassId)
                    .orElseThrow(() -> new IllegalArgumentException("Could not find school class with id: " + schoolClassId));
        }
        return generateEmptyScheduleForSchoolClass(schoolClassFound);
    }

    private ClassSchedule generateEmptyScheduleForSchoolClass(SchoolClass schoolClass) {
        //TODO: refactor and elevate to other service
        ClassSchedule schedule = new ClassSchedule();
        schedule.setClassScheduleName(schoolClass.className() + " SCHEDULE");
        ClassSchedule classSchedule = classScheduleRepository.save(schedule);

        List<ScheduleEntry> entries = new LinkedList<>();
        for (DayOfWeek day : EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)) {
            ScheduleEntry currentEntry = scheduleEntryRepository.save(new ScheduleEntry(classSchedule, schoolClass, day, new LinkedList<>()));
            LinkedList<TimeSlot> timeSlots = buildTimeSlots(currentEntry);
            currentEntry.setTimeSlots(timeSlots);
            scheduleEntryRepository.save(currentEntry);
            entries.add(currentEntry);
        }

        classSchedule.setScheduleEntries(entries);
        ClassSchedule toReturn = classScheduleRepository.save(classSchedule);
        System.out.println(toReturn);
        return toReturn;
    }

    private LinkedList<TimeSlot> buildTimeSlots(ScheduleEntry scheduleEntry) {
        LinkedList<TimeSlot> timeSlots = new LinkedList<>();
        int maxTimeSlots = (applicationConfig.getMaxLessons() * 2) - 1;
        int lessonDuration = applicationConfig.getLessonDuration();
        int shortBreakDuration = applicationConfig.getShortBreakDuration();
        int longBreakDuration = applicationConfig.getLongBreakDuration();

        LocalTime timeSlotBeginning = applicationConfig.getLessonScheduleStart();
        LocalTime timeSlotEnding = timeSlotBeginning.plusMinutes(lessonDuration);

        TimeSlot toSave = new TimeSlot(
                timeSlotBeginning,
                timeSlotEnding,
                null,
                scheduleEntry,
                false
        );
        TimeSlot savedTimeSlot = timeSlotRepository.save(toSave);
        timeSlots.add(savedTimeSlot);

        boolean slotIsBreak;
        int currentTimeSlot = 2;
        while (currentTimeSlot <= maxTimeSlots) {
            timeSlotBeginning = timeSlotEnding;
            if (slotIsBreak(currentTimeSlot)) {
                slotIsBreak = true;
                if (slotIsLongBreak(currentTimeSlot)) {
                    timeSlotEnding = timeSlotEnding.plusMinutes(longBreakDuration);
                } else {
                    timeSlotEnding = timeSlotEnding.plusMinutes(shortBreakDuration);
                }
            } else {
                slotIsBreak = false;
                timeSlotEnding = timeSlotEnding.plusMinutes(lessonDuration);
            }
            savedTimeSlot = timeSlotRepository.save(
                    new TimeSlot(
                            timeSlotBeginning,
                            timeSlotEnding,
                            null,
                            scheduleEntry,
                            slotIsBreak
                    )
            );
            currentTimeSlot++;
            timeSlots.add(savedTimeSlot);
        }
        return timeSlots;
    }

    private boolean slotIsBreak(int timeSlot) {
        return timeSlot % 2 == 0;
    }

    private boolean slotIsLongBreak(int timeSlot) {
        int firstLongBreak = applicationConfig.getFirstLongBreak() * 2;
        int secondLongBreak = applicationConfig.getSecondLongBreak() * 2;
        return timeSlot == firstLongBreak || timeSlot == secondLongBreak;
    }
}
