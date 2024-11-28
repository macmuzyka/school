package com.school.service

import com.school.repository.StudentDuplicateErrorRepository
import com.schoolmodel.model.dto.StudentDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DuplicatedStudentService(
        private val studentDuplicateErrorRepository: StudentDuplicateErrorRepository
) {
    private val log = LoggerFactory.getLogger(DuplicatedStudentService::class.java)
    fun getAllDuplicatedStudents(): List<StudentDTO> = studentDuplicateErrorRepository.findAll().map { StudentDTO(it) }.toList()

    fun deleteStudent(studentId: Long) {
        log.info("Passed student id to remove $studentId")
        studentDuplicateErrorRepository.deleteById(studentId)
        log.info("Removing duplicated student with id $studentId DONE")
    }

    fun deleteAll() = studentDuplicateErrorRepository.deleteAll()

    fun deleteDuplicatedStudentsWithIds(ids: List<Long>) {
        log.info("Student duplicated IDs passed to be removed: $ids")
        studentDuplicateErrorRepository.deleteAllById(ids)
        log.info("Deleting of duplicated students with IDs $ids DONE")
    }
}
