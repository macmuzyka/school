package com.school.service.attendance

import com.school.model.StudentPresenceDTO
import com.school.model.entity.Student
import com.school.model.entity.attendance.StudentPresence
import com.school.service.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentPresenceService(
    private val studentService: StudentService
) {
    private val log = LoggerFactory.getLogger(StudentPresenceService::class.java)
    fun buildStudentPresenceEntities(presenceList: List<StudentPresenceDTO>) =
        presenceList
            .map { sp ->
                studentService.getStudent(sp.studentId)
                    ?.let { StudentPresence(it, sp.presenceStatus, sp.note) } ?: StudentPresence(
                    Student(),
                    sp.presenceStatus,
                    sp.note
                )
            }.toList()
}