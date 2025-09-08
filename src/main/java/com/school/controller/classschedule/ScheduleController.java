package com.school.controller.classschedule;

import com.school.service.classschedule.ClassScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ClassScheduleService classScheduleService;

    public ScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    @GetMapping
    public ResponseEntity<?> getSchedules() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(classScheduleService.getSchedulesSummary());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/purge")
    public ResponseEntity<?> purgeSchedule(@RequestParam Long classId) {
        try {
            classScheduleService.purgeClassSchedule(classId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
