package com.school.classschedule;

import com.school.WarmupDatabasePopulation;
import com.school.configuration.ApplicationConfig;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.classschedule.ClassScheduleRepository;
import com.school.repository.classschedule.ScheduleEntryRepository;
import com.school.repository.classschedule.TimeSlotRepository;
import com.school.service.*;
import com.school.service.classschedule.ClassScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @Autowired
    private ClassScheduleRepository classScheduleRepository;
    @Autowired
    private ScheduleEntryRepository scheduleEntryRepository;
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

    @BeforeEach
    public void setUp() throws IOException {
        File studentsFile = new File("src/test/resources/students_list.txt");
        InputStream inputStream = new FileInputStream(studentsFile);
        MultipartFile mpf = new MockMultipartFile("file", studentsFile.getName(), "text/plain", inputStream);
        inputStudentsFromTextFileService.addStudents(mpf);
        seedGradesService.seedStudentsWithRandomizedGrades();
    }

    @Disabled
    @Test
    public void countGradesOnWarmup() {
        assertEquals(applicationConfig.getGradesToAdd(), gradeRepository.count());
    }

    @Test
    public void createSampleClassScheduleForDefaultConditions() {
        assertEquals(415, calculateDurationFromGeneratedSchedule(classScheduleService.generateEmptyScheduleForSchoolClass(1L)));
    }

    private static long calculateDurationFromGeneratedSchedule(ClassSchedule classSchedule) {
        LocalTime scheduleBeginning = classSchedule.getScheduleEntries().get(0).getTimeSlots().getFirst().getStartTime();
        LocalTime scheduleEnding = classSchedule.getScheduleEntries().get(0).getTimeSlots().getLast().getEndTime();
        return Duration.between(scheduleBeginning, scheduleEnding).toMinutes();
    }
}
