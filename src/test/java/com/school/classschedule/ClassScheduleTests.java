package com.school.classschedule;

import com.school.WarmupDatabasePopulation;
import com.school.configuration.ApplicationConfig;
import com.school.model.dto.sclassschedule.ClassScheduleDisplayDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"devel"})
@Import({InputStudentsFromTextFileService.class,
        StudentsFromListBuilderService.class,
        ClassService.class,
        SendNotificationToFrontendService.class,
        SeedGradesService.class,
        ClassScheduleService.class,
        WarmupDatabasePopulation.class
})
public class ClassScheduleTests {
    private static final Logger log = LoggerFactory.getLogger(ClassScheduleTests.class);
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
    private ClassScheduleService classScheduleService;

    @Test
    public void createSampleClassScheduleForDefaultConditions() {
        long scheduleTimeFromApplicationConfig = calculateScheduleTimeFromApplicationConfig();
        long scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot = calculateDurationFromGeneratedSchedule(classScheduleService.generateEmptyScheduleForSchoolClass(1L));
        assertEquals(scheduleTimeFromApplicationConfig, scheduleTimeFromCalculatedDurationFromFirstAndLastTimeslot);
    }

    private long calculateScheduleTimeFromApplicationConfig() {
        int lessonsDuration = applicationConfig.getMaxLessons() * applicationConfig.getLessonDuration();
        int numberOfLongBreaks = (applicationConfig.getFirstLongBreak() != 0 ? 1 : 0) + (applicationConfig.getSecondLongBreak() != 0 ? 1 : 0);
        int longBreaksDuration = applicationConfig.getLongBreakDuration() * numberOfLongBreaks;
        int shortBreaksDuration = (applicationConfig.getMaxLessons() - 1 - numberOfLongBreaks) * applicationConfig.getShortBreakDuration();
        return (lessonsDuration + longBreaksDuration + shortBreaksDuration);
    }

    private long calculateDurationFromGeneratedSchedule(ClassSchedule classSchedule) {
        List<TimeSlot> firstTimeslots = classSchedule.getScheduleEntries().get(0).getTimeSlots();
        LocalTime scheduleBeginning = firstTimeslots.get(0).getStartTime();
        LocalTime scheduleEnding = classSchedule.getScheduleEntries().get(0).getTimeSlots().get(firstTimeslots.size() - 1).getEndTime();
        return Duration.between(scheduleBeginning, scheduleEnding).toMinutes();
    }

    @Test
    public void shouldProperlyMapDatabaseResultsToDTOObjects() {
        classScheduleService.generateEmptyScheduleForSchoolClass(1L);
        Map<String, ClassScheduleDisplayDTO> dto = classScheduleService.classScheduleDisplayDTO();
        System.out.println("dto.size() : " + dto.size());
        System.out.println("dto.toString() : " + dto.toString());
        ClassScheduleDisplayDTO anyKey = dto.values().stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find any records in a test map!"));

        assertInstanceOf(Map.class, dto);
        assertInstanceOf(ClassScheduleDisplayDTO.class, anyKey);
    }
}
