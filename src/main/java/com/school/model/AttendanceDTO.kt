package com.school.model

import com.school.service.attendance.PresenceStatus

data class AttendanceDTO(
    val timeSlotId: Long,
    val studentPresence: List<StudentPresenceDTO>
)

data class StudentPresenceDTO(val studentId: Long, val name: String, val surname: String, val presenceStatus: PresenceStatus, val note: String)
