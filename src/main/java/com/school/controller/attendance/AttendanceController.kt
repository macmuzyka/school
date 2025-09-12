package com.school.controller.attendance

import com.school.model.AttendanceDTO
import com.school.service.attendance.AttendanceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/attendance")
@Tag(name = "Attendance", description = "Endpoints for managing attendance")
class AttendanceController(
    private val attendanceService: AttendanceService
) {
    private val log = LoggerFactory.getLogger(AttendanceController::class.java)

    @Operation(summary = "Submit attendance", description = "Save attendance for a given timeslot")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Attendance saved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping("/submit")
    fun saveAttendance(@RequestBody attendance: AttendanceDTO): ResponseEntity<*> = try {
        ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.saveAttendance(attendance))
    } catch (e: Exception) {
        log.error(e.message)
        e.printStackTrace()
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }

    @Operation(summary = "Get attendance", description = "Retrieve attendance by time slot ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Attendance found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/get")
    fun getAttendance(@RequestParam timeSlotId: Long): ResponseEntity<*> = try {
        ResponseEntity.status(HttpStatus.OK).body(attendanceService.getAttendanceByTimeSlotId(timeSlotId))
    } catch (e: Exception) {
        log.error(e.message)
        e.printStackTrace()
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }
}