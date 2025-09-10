package com.school.controller.attendance

import com.school.model.AttendanceDTO
import com.school.service.attendance.AttendanceService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/attendance")
class AttendanceController(
    private val attendanceService: AttendanceService
) {
    private val log = LoggerFactory.getLogger(AttendanceController::class.java)

    @PostMapping("/submit")
    fun saveAttendance(@RequestBody attendance: AttendanceDTO): ResponseEntity<*> = try {
        ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.saveAttendance(attendance))
    } catch (e: Exception) {
        log.error(e.message)
        e.printStackTrace()
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }

    @GetMapping("/get")
    fun getAttendance(@RequestParam timeSlotId: Long): ResponseEntity<AttendanceDTO> = try {
        ResponseEntity.status(HttpStatus.OK).body(attendanceService.getAttendanceByTimeSlotId(timeSlotId))
    } catch (e: Exception) {
        log.error(e.message)
        e.printStackTrace()
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AttendanceDTO(timeSlotId, mutableListOf()))
    }
}