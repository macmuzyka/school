package com.school.controller.classschedule;

import com.school.service.classschedule.TimeSlotAssigningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timeslot-assigning")
public class TimeSlotAssigningController {
    private final TimeSlotAssigningService timeSlotAssigningService;

    public TimeSlotAssigningController(TimeSlotAssigningService timeSlotAssigningService) {
        this.timeSlotAssigningService = timeSlotAssigningService;
    }

    @PostMapping("/add-to-timeslot")
    public ResponseEntity<?> addSubjectToTimeSlot(@RequestParam Long subjectId, @RequestParam Long timeSlotId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(timeSlotAssigningService.assignSubjectToTimeSlot(subjectId, timeSlotId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
