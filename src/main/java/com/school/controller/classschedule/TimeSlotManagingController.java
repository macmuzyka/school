package com.school.controller.classschedule;

import com.school.service.classschedule.TimeSlotManagingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timeslot-managing")
public class TimeSlotManagingController {
    private final TimeSlotManagingService timeSlotManagingService;

    public TimeSlotManagingController(TimeSlotManagingService timeSlotManagingService) {
        this.timeSlotManagingService = timeSlotManagingService;
    }

    @PostMapping("/add-to-timeslot")
    public ResponseEntity<?> addSubjectToTimeSlot(@RequestParam Long subjectId, @RequestParam Long timeSlotId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(timeSlotManagingService.assignSubjectToTimeSlot(subjectId, timeSlotId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
