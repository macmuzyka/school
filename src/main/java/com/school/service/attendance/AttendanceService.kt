package com.school.service.attendance

import com.school.model.AttendanceDTO
import com.school.model.dto.AttendanceResponse
import com.school.repository.attendance.AttendanceRepository
import com.school.service.classschedule.TimeSlotQueryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val timeSlotQueryService: TimeSlotQueryService,
    private val studentPresentService: StudentPresenceService,
) {
    private val log = LoggerFactory.getLogger(AttendanceService::class.java)

    fun removeAttendance(attendanceId: Long) = attendanceRepository.deleteById(attendanceId)

    @Transactional
    fun saveAttendance(request: AttendanceDTO): AttendanceResponse =
        request
            .prepareEntity(timeSlotQueryService.getTimeSlotById(request.timeslotId), studentPresentService)
            .associateAttendanceWithPresence()
            .persist(attendanceRepository)
            .respond()
}