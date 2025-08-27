package com.school.controller.attendance

import com.school.model.AttendanceDTO
import com.school.service.attendance.AttendanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/attendance")
class AttendanceController(
    private val attendanceService: AttendanceService
) {
    fun saveAttendance(@RequestBody attendance: AttendanceDTO) = try {
        attendanceService.saveAttendance(attendance)
    } catch (e: Exception) {
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }
}