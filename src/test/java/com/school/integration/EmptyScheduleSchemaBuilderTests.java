package com.school.integration;

import com.school.configuration.ClassScheduleConfig;
import com.school.model.dto.sclassschedule.DaySubject;
import com.school.model.entity.Audit;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.service.classschedule.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("prod")
public class EmptyScheduleSchemaBuilderTests {
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassScheduleConfig classScheduleConfig;
    private final ClassScheduleService classScheduleService;

    @Autowired
    public EmptyScheduleSchemaBuilderTests(ClassScheduleRepository classScheduleRepository, ClassScheduleConfig classScheduleConfig, ClassScheduleService classScheduleService) {
        this.classScheduleRepository = classScheduleRepository;
        this.classScheduleConfig = classScheduleConfig;
        this.classScheduleService = classScheduleService;
    }

    @Test
    @Transactional
    public void scheduleBeginningSlotAndEndSlotShouldMatchApplicationConfigValues() {
        long scheduleTimeFromApplicationConfig = calculateScheduleTotalDurationFromApplicationConfig();
        long scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot =
                calculateScheduleTotalDurationFromGeneratedSchedule(findAnyClassSchedule());
        assertEquals(scheduleTimeFromApplicationConfig, scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot);
    }

    private ClassSchedule findAnyClassSchedule() {
        return classScheduleRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow();
    }

    private long calculateScheduleTotalDurationFromGeneratedSchedule(ClassSchedule classSchedule) {
        List<TimeSlot> anyEntryTimeslots = findAnyEntryTimeSlots(classSchedule);
        anyEntryTimeslots.sort(Comparator.comparingLong(Audit::getId));
        LocalTime scheduleBeginning = findEntryTimeSlotsBeginning(anyEntryTimeslots);
        LocalTime scheduleEnding = findScheduleEnding(classSchedule, anyEntryTimeslots);
        return Duration.between(scheduleBeginning, scheduleEnding).toMinutes();
    }

    private LocalTime findEntryTimeSlotsBeginning(List<TimeSlot> entryTimeSlots) {
        return entryTimeSlots.stream()
                .findFirst()
                .orElseThrow()
                .getStartTime();
    }

    private List<TimeSlot> findAnyEntryTimeSlots(ClassSchedule classSchedule) {
        return classSchedule.getScheduleEntries().stream()
                .findAny()
                .orElseThrow()
                .getTimeSlots();
    }

    private LocalTime findScheduleEnding(ClassSchedule classSchedule, List<TimeSlot> entryTimeSlots) {
        int timeslotsLastIndex = entryTimeSlots.size() - 1;
        return classSchedule.getScheduleEntries().get(0).getTimeSlots().get(timeslotsLastIndex).getEndTime();
    }

    private long calculateScheduleTotalDurationFromApplicationConfig() {
        int lessonsDuration = classScheduleConfig.getMaxClassPerDay() * classScheduleConfig.getLessonDuration();
        int numberOfLongBreaks = (longBreakNumberOne()) + (longBreakNumberTwo());
        int longBreaksDuration = numberOfLongBreaks * classScheduleConfig.getLongBreakDuration();
        int shortBreaksDuration = (actualNumberOfShortBreaks() - numberOfLongBreaks) * classScheduleConfig.getShortBreakDuration();
        return (lessonsDuration + longBreaksDuration + shortBreaksDuration);
    }

    private int longBreakNumberOne() {
        return classScheduleConfig.getFirstLongBreak() != 0 ? 1 : 0;
    }

    private int longBreakNumberTwo() {
        return classScheduleConfig.getSecondLongBreak() != 0 ? 1 : 0;
    }

    private int actualNumberOfShortBreaks() {
        return classScheduleConfig.getMaxClassPerDay() - 1;
    }

    @Test
    @Transactional
    public void shouldProperlyMapDatabaseResultsToDisplayScheduleWithoutErrors() {
        Long anyScheduleId = classScheduleRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow()
                .getId();

        Map<String, List<DaySubject>> scheduleDisplay = classScheduleService
                .getClassScheduleGroupedByDaySubjectAndTimeframe(anyScheduleId, false);
        DaySubject anyKey = scheduleDisplay.values().stream()
                .flatMap(Collection::stream)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find any DaySubject from mapped object to display!"));

        assertInstanceOf(Map.class, scheduleDisplay);
        assertInstanceOf(DaySubject.class, anyKey);
    }
}
