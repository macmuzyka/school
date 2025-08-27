package com.school.model

import com.school.service.attendance.PresenceStatus

data class AttendanceDTO(
    val timeslotId: Long,
    val studentPresence: List<StudentPresenceDTO>
)

data class StudentPresenceDTO(val studentId: Long, val presenceStatus: PresenceStatus, val note: String)
