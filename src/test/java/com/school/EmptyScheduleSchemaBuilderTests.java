package com.school;

import com.school.configuration.ApplicationConfig;
import com.school.configuration.ClassScheduleConfig;
import com.school.model.dto.sclassschedule.DaySubject;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.repository.classschedule.ScheduleEntryRepository;
import com.school.repository.classschedule.TimeSlotRepository;
import com.school.service.*;
import com.school.service.classschedule.ClassScheduleService;
import com.school.service.classschedule.EmptyScheduleSchemaBuilderService;
import com.school.service.classschedule.TimeSlotBuilderService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("prod")
@Import({InputStudentsFromTextFileService.class,
        StudentsFromListBuilderService.class,
        SchoolClassService.class,
        SendNotificationToFrontendService.class,
        SeedGradesService.class,
        SeedGradeWorker.class,
        EmptyScheduleSchemaBuilderService.class,
        ClassScheduleService.class,
        TimeSlotBuilderService.class,
        WarmupDatabasePopulation.class
})
public class EmptyScheduleSchemaBuilderTests {
    private static final Logger log = LoggerFactory.getLogger(EmptyScheduleSchemaBuilderTests.class);
    @Autowired
    private ClassScheduleRepository classScheduleRepository;
    @Autowired
    private ScheduleEntryRepository scheduleEntryRepository;
    @Autowired
    private StudentRepository studentRepository;
    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private InputStudentsFromTextFileService inputStudentsFromTextFileService;
    @Autowired
    private SeedGradesService seedGradesService;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private ClassScheduleConfig classScheduleConfig;
    @Autowired
    private ClassScheduleService classScheduleService;
    @Autowired
    private EmptyScheduleSchemaBuilderService emptyScheduleSchemaBuilderService;
    @Autowired
    private TimeSlotBuilderService timeSlotBuilderService;

    @Test
    public void scheduleBeginningSlotAndEndSlotShouldMatchApplicationConfigValues() {
        long scheduleTimeFromApplicationConfig = calculateScheduleTotalDurationFromApplicationConfig();
        long scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot =
                calculateScheduleTotalDurationFromGeneratedSchedule(classScheduleRepository
                        .findAll()
                        .stream()
                        .findAny()
                        .orElseThrow(() ->
                                new IllegalStateException("Application warmup phase did not execute correctly, " +
                                        "could not find any schedule!")
                        )
                );
        assertEquals(scheduleTimeFromApplicationConfig, scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot);
    }

    private long calculateScheduleTotalDurationFromGeneratedSchedule(ClassSchedule classSchedule) {
        List<TimeSlot> firstTimeslots = classSchedule.getScheduleEntries().get(0).getTimeSlots();
        LocalTime scheduleBeginning = firstTimeslots.get(0).getStartTime();
        LocalTime scheduleEnding = classSchedule.getScheduleEntries().get(0).getTimeSlots().get(firstTimeslots.size() - 1).getEndTime();
        return Duration.between(scheduleBeginning, scheduleEnding).toMinutes();
    }

    private long calculateScheduleTotalDurationFromApplicationConfig() {
        int lessonsDuration = classScheduleConfig.getMaxLessonPerDay() * classScheduleConfig.getLessonDuration();
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
        return classScheduleConfig.getMaxLessonPerDay() - 1;
    }

    @Test
    public void shouldProperlyMapDatabaseResultsToDisplayScheduleWithoutErrors() {
        Long anyScheduleId = classScheduleRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow(() ->
                        new IllegalStateException("Application warmup phase did not execute correctly, " +
                                "could not find any schedule!"))
                .getId();

        Map<String, List<DaySubject>> scheduleDisplay = classScheduleService
                .getClassScheduleGroupedByDaySubjectAndTimeframe(anyScheduleId, false);
        System.out.println("Numbers of schedules: " + scheduleDisplay.size());
        System.out.println("Timeframes display: ");

        for (String timeframe : scheduleDisplay.keySet()) {
            System.out.println("[" + timeframe + "]");
        }
        DaySubject anyKey = scheduleDisplay.values().stream()
                .flatMap(Collection::stream)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find any DaySubject from mapped object to display!"));

        assertInstanceOf(Map.class, scheduleDisplay);
        assertInstanceOf(DaySubject.class, anyKey);
    }
}
