package com.school.classschedule;

import com.school.service.classschedule.TimeSlotManagingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//TODO: they should be written BEFORE implementing business logic, but do it asap
public class PreviousOrNextSlotsTakenTests {
    private final TimeSlotManagingService timeSlotManagingService;

    public PreviousOrNextSlotsTakenTests(final TimeSlotManagingService timeSlotManagingService) {
        this.timeSlotManagingService = timeSlotManagingService;
    }

    @Test
    public void shouldFindPreviousOrNextTimeSlotTaken() {
        //add some subjects to timeslots then test
    }

    @Test
    public void shouldNotFindPreviousOrNextSlotsTaken() {
        timeSlotManagingService.assignSubjectToTimeSlot(1L, 3L);
    }
}
