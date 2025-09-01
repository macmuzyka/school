package com.school.integration;

import com.school.model.entity.Audit;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.SchoolClassRepository;
import com.school.repository.SubjectRepository;
import com.school.service.SchoolClassService;
import com.school.service.classschedule.AdjacentTimeSlotsUtils;
import com.school.service.classschedule.TimeSlotManagingService;
import com.school.service.classschedule.TimeSlotQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
@ActiveProfiles("prod")
public class PreviousOrNextSlotsTakenTests {
    private final TimeSlotManagingService timeSlotManagingService;
    private final TimeSlotQueryService timeSlotQueryService;
    private final SubjectRepository subjectRepository;
    private final SchoolClassService schoolClassService;
    private final SchoolClassRepository schoolClassRepository;
    private SchoolClass testClass;
    private ScheduleEntry entry;
    private List<TimeSlot> sortedTimeSlots;
    private Subject subject;

    @Autowired
    public PreviousOrNextSlotsTakenTests(TimeSlotManagingService timeSlotManagingService,
                                         TimeSlotQueryService timeSlotQueryService,
                                         SubjectRepository subjectRepository,
                                         SchoolClassService schoolClassService,
                                         SchoolClassRepository schoolClassRepository) {
        this.timeSlotManagingService = timeSlotManagingService;
        this.timeSlotQueryService = timeSlotQueryService;
        this.subjectRepository = subjectRepository;
        this.schoolClassService = schoolClassService;
        this.schoolClassRepository = schoolClassRepository;
    }

    @BeforeEach
    public void setUp() {
        testClass = schoolClassService.createNewClass();
        entry = testClass.getClassSchedule().getScheduleEntries().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find any schedule entry with assigned time slots for test class: " + testClass));
        sortedTimeSlots = new ArrayList<>(entry.getTimeSlots().stream().filter(TimeSlot::isNotBreak).toList());
        sortedTimeSlots.sort(Comparator.comparing(Audit::getId));
        subject = subjectRepository.findBySchoolClassId(entry.getClassSchedule().getSchoolClass().getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find any subject for given school class id:" + entry.getId()));
    }

    @AfterEach
    public void cleanup() {
        schoolClassRepository.delete(testClass);
    }

    @Test
    @Transactional
    public void shouldNotFindAdjacentSlotsTaken() {
        int secondClassThatCanButDoesNotHaveAdjacentTakenTimeSlot = 1;
        TimeSlot secondClassSlot = sortedTimeSlots.get(secondClassThatCanButDoesNotHaveAdjacentTakenTimeSlot);

        List<TimeSlot> scheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(entry);

        timeSlotManagingService.assignSubjectToTimeSlot(subject.getId(), secondClassSlot.getId());
        assertNull(AdjacentTimeSlotsUtils.lookForPreviousOrNextTimeSlotWithMatchingSubject(scheduleEntryTimeSlots, secondClassSlot, subject));
    }

    @Test
    @Transactional
    public void shouldFindAdjacentSlotsTaken() {
        int savedBeforeOtherTimeSlotNo = 3;
        TimeSlot savedBeforeOtherTimeSlot = sortedTimeSlots.get(savedBeforeOtherTimeSlotNo);
        timeSlotManagingService.assignSubjectToTimeSlot(subject.getId(), savedBeforeOtherTimeSlot.getId());

        int slotNoToFindAdjacentNo = 2;
        TimeSlot slotToFindAdjacent = sortedTimeSlots.get(slotNoToFindAdjacentNo);
        List<TimeSlot> scheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(savedBeforeOtherTimeSlot.getScheduleEntry());

        assertEquals(savedBeforeOtherTimeSlot.getScheduleEntry().getId(), slotToFindAdjacent.getScheduleEntry().getId());
        assertNotNull(AdjacentTimeSlotsUtils.lookForPreviousOrNextTimeSlotWithMatchingSubject(scheduleEntryTimeSlots, slotToFindAdjacent, subject));
    }
}
