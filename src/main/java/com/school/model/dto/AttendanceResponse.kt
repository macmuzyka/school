package com.school.model.dto

import com.school.model.entity.attendance.Attendance
import com.school.service.attendance.PresenceStatus
import java.time.LocalDate

data class AttendanceResponse(
    var id: Long,
    var attendanceTimeframe: LocalDate,
    var subject: String,
    val presenceList: Map<PresenceStatus, List<String>>
) {
    constructor(attendance: Attendance) : this(
        attendance.id,
        attendance.attendanceDate,
        attendance.subject.name,
        attendance.presenceList.groupBy { it.presenceStatus() }
            .mapValues { students ->
                students.value
                    .map { it.student.name + " " + it.student.surname }
            }
    )
}
