package com.school;

import com.school.model.entity.Audit;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.SubjectRepository;
import com.school.repository.classschedule.ScheduleEntryRepository;
import com.school.repository.classschedule.TimeSlotRepository;
import com.school.service.classschedule.AdjacentTimeSlotsUtils;
import com.school.service.classschedule.TimeSlotManagingService;
import com.school.service.classschedule.TimeSlotQueryService;
import com.school.service.utils.TimeSlotUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("prod")
public class PreviousOrNextSlotsTakenTests {
    private final TimeSlotManagingService timeSlotManagingService;
    private final TimeSlotQueryService timeSlotQueryService;
    private final ScheduleEntryRepository scheduleEntryRepository;
    private final SubjectRepository subjectRepository;
    private ScheduleEntry entry;
    private List<TimeSlot> sortedTimeSlots;
    private Subject subject;

    @Autowired
    public PreviousOrNextSlotsTakenTests(TimeSlotManagingService timeSlotManagingService,
                                         TimeSlotQueryService timeSlotQueryService,
                                         ScheduleEntryRepository scheduleEntryRepository,
                                         SubjectRepository subjectRepository) {
        this.timeSlotManagingService = timeSlotManagingService;
        this.timeSlotQueryService = timeSlotQueryService;
        this.scheduleEntryRepository = scheduleEntryRepository;
        this.subjectRepository = subjectRepository;
    }

    @BeforeEach
    public void setUp() {
        entry = scheduleEntryRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find any schedule entry with assigned time slots!"));
        sortedTimeSlots = new ArrayList<>(entry.getTimeSlots().stream().filter(TimeSlotUtils::isNotBreak).toList());
        sortedTimeSlots.sort(Comparator.comparing(Audit::getId));
        subject = subjectRepository.findBySchoolClassId(entry.getClassSchedule().getSchoolClass().getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find any subject for given school class id:" + entry.getId()));
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
