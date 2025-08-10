package com.school.classschedule;

import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.service.SubjectService;
import com.school.service.classschedule.AdjacentTimeSlotsUtils;
import com.school.service.classschedule.TimeSlotManagingService;
import com.school.service.classschedule.TimeSlotQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("prod")
public class PreviousOrNextSlotsTakenTests {
    private final TimeSlotManagingService timeSlotManagingService;
    private final TimeSlotQueryService timeSlotQueryService;
    private final SubjectService subjectService;
    private final long secondLesson = 3L;
    private final long thirdLesson = 5L;
    private final long fourthLesson = 7L;
    private final long firstSubject = 1L;

    @Autowired
    public PreviousOrNextSlotsTakenTests(TimeSlotManagingService timeSlotManagingService, TimeSlotQueryService timeSlotQueryService, SubjectService subjectService) {
        this.timeSlotManagingService = timeSlotManagingService;
        this.timeSlotQueryService = timeSlotQueryService;
        this.subjectService = subjectService;
    }


    @Test
    @Transactional
    public void shouldNotFindAdjacentSlotsTaken() {
        TimeSlot timeSlot = timeSlotQueryService.getTimeSlotById(secondLesson);
        List<TimeSlot> scheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(timeSlot.getScheduleEntry());
        Subject subject = subjectService.getSubjectById(firstSubject);

        timeSlotManagingService.assignSubjectToTimeSlot(firstSubject, secondLesson);
        assertNull(AdjacentTimeSlotsUtils.findPreviousOrNextTimeSlotWithMatchingSubject(scheduleEntryTimeSlots, timeSlot, subject));
    }

    @Test
    @Transactional
    public void shouldFindAdjacentSlotsTaken() {
        TimeSlot timeSlot = timeSlotQueryService.getTimeSlotById(fourthLesson);
        List<TimeSlot> scheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(timeSlot.getScheduleEntry());
        Subject subject = subjectService.getSubjectById(firstSubject);

        timeSlotManagingService.assignSubjectToTimeSlot(firstSubject, thirdLesson);
        assertNotNull(AdjacentTimeSlotsUtils.findPreviousOrNextTimeSlotWithMatchingSubject(scheduleEntryTimeSlots, timeSlot, subject));
    }
}
