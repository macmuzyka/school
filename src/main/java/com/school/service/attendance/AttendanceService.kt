package com.school.service.attendance

import com.school.model.AttendanceDTO
import com.school.model.StudentPresenceDTO
import com.school.model.dto.AttendanceResponse
import com.school.repository.attendance.AttendanceRepository
import com.school.service.SchoolClassService
import com.school.service.classschedule.TimeSlotQueryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val timeSlotQueryService: TimeSlotQueryService,
    private val studentPresentService: StudentPresenceService,
    private val schoolClassService: SchoolClassService
) {
    private val log = LoggerFactory.getLogger(AttendanceService::class.java)

    fun removeAttendance(attendanceId: Long) = attendanceRepository.deleteById(attendanceId)

    @Transactional
    fun saveAttendance(request: AttendanceDTO): AttendanceResponse {
        removePreviousAttendance(request.timeSlotId)
        return request.prepareAttendanceEntity(
            timeSlotQueryService.getTimeSlotById(request.timeSlotId),
            studentPresentService
        ).associateAttendanceWithPresence()
            .persist(attendanceRepository)
            .asResponse()
    }

    private fun removePreviousAttendance(timeSlotId: Long) =
        attendanceRepository.findByTimeSlotId(timeSlotId).takeIf { it.isPresent }?.get()
            ?.let { attendanceRepository.delete(it) }

    fun getAttendanceByTimeSlotId(timeSlotId: Long): AttendanceDTO {
        return attendanceRepository.findByTimeSlotId(timeSlotId)
            .takeIf { it.isPresent }
            ?.get()
            ?.let { savedAttendance ->
                AttendanceDTO(
                    timeSlotId = savedAttendance.timeSlot.id,
                    studentPresence = savedAttendance.presenceList
                        .map { presenceRecord ->
                            StudentPresenceDTO(
                                studentId = presenceRecord.student.id,
                                name = presenceRecord.student.name,
                                surname = presenceRecord.student.surname,
                                presenceStatus = presenceRecord.presenceStatus(),
                                note = presenceRecord.note
                            )
                        }).also { log.info("Returning already saved attendance for time slot with id: $timeSlotId") }
            }
            ?: getEmptyAttendanceList(timeSlotId).also { log.info("Returning new attendance for time slot with id: $timeSlotId") }
    }

    private fun getEmptyAttendanceList(timeSlotId: Long) = AttendanceDTO(
        timeSlotId,
        schoolClassService.getSchoolClassById(resolveClassIdForTimeSlot(timeSlotId)).classStudents
            .map { student ->
                StudentPresenceDTO(
                    studentId = student.id,
                    name = student.name,
                    surname = student.surname,
                    presenceStatus = PresenceStatus.UNSPECIFIED,
                    note = ""
                )
            })

    private fun resolveClassIdForTimeSlot(timeSlotId: Long): Long {
        return timeSlotQueryService.getTimeSlotById(timeSlotId).scheduleEntry.classSchedule.schoolClass.id
    }
}