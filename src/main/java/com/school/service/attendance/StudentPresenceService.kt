package com.school.service.attendance

import com.school.model.StudentPresenceDTO
import com.school.model.entity.Student
import com.school.model.entity.attendance.StudentPresence
import com.school.service.StudentService
import org.springframework.stereotype.Service

@Service
open class StudentPresenceService(
    private val studentService: StudentService
) {
    fun buildEntitiesFromDTO(presenceList: List<StudentPresenceDTO>) =
        presenceList
            .map { sp ->
                studentService.getStudent(sp.studentId)
                    ?.let { StudentPresence(it, sp.presenceStatus, sp.note) } ?: StudentPresence(Student(), sp.presenceStatus, sp.note)
            }.toList()
}