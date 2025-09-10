package com.school.service.attendance

import com.school.model.AttendanceDTO
import com.school.model.dto.AttendanceResponse
import com.school.model.entity.attendance.Attendance
import com.school.model.entity.classschedule.TimeSlot
import com.school.repository.attendance.AttendanceRepository
import java.time.LocalDate

fun AttendanceDTO.prepareAttendanceEntity(timeSlot: TimeSlot, studentPresenceService: StudentPresenceService): Attendance =
    Attendance(
        timeSlot.startTime,
        timeSlot.endTime,
        LocalDate.now(),
        timeSlot.subject,
        timeSlot,
        studentPresenceService.buildStudentPresenceEntities(studentPresence)
    )

fun Attendance.associateAttendanceWithPresence(): Attendance = apply { presenceList.forEach { it.attendance = this } }

fun Attendance.asResponse(): AttendanceResponse = AttendanceResponse(this)

fun Attendance.persist(attendanceRepository: AttendanceRepository): Attendance = attendanceRepository.save(this)