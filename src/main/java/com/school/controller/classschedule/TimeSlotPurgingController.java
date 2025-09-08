package com.school.controller.classschedule;

import com.school.service.classschedule.TimeSlotPurgingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timeslot-purging")
public class TimeSlotPurgingController {
    private final TimeSlotPurgingService timeSlotPurgingService;

    public TimeSlotPurgingController(TimeSlotPurgingService timeSlotPurgingService) {
        this.timeSlotPurgingService = timeSlotPurgingService;
    }

    @PatchMapping
    public ResponseEntity<?> purgeTimeSlot(@RequestParam Long timeSlotId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(timeSlotPurgingService.purgeTimeSlot(timeSlotId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
