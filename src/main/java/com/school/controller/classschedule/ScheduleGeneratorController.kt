package com.school.controller.classschedule

import com.school.service.classschedule.ClassScheduleSeederService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/schedule-generator")
class ScheduleGeneratorController(private val classScheduleSeederService: ClassScheduleSeederService) {
    @PostMapping
    fun generateSchedule(@RequestParam classId: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(classScheduleSeederService.seedScheduleWithClasses(classId))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: $e")
        }
    }
}