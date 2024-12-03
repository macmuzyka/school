package com.school.service

import com.school.repository.StudentInsertErrorRepository
import com.school.repository.StudentRepository
import com.school.model.dto.StudentDTO
import com.school.model.entity.Student
import com.school.model.entity.StudentInsertError
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class InsertErrorStudentService(
        private val studentInsertErrorRepository: StudentInsertErrorRepository,
        private val studentRepository: StudentRepository
) {
    private val log = LoggerFactory.getLogger(InsertErrorStudentService::class.java)

    fun correctStudent(correctedStudent: StudentDTO): Student {
        log.info("correctedStudent: $correctedStudent")
        studentInsertErrorRepository.findById(correctedStudent.id).takeIf { it.isPresent }?.get()?.let { toRemove ->
            val corrected = prepareStudent(correctedStudent)
            return saveCorrectedAndRemoveStudentInsertError(corrected, toRemove)
        } ?: throw IllegalArgumentException("Error record student with id " + correctedStudent.id + " not found!")
    }

    private fun prepareStudent(correctedStudent: StudentDTO): Student {
        return Student(
                correctedStudent.name,
                correctedStudent.surname,
                correctedStudent.identifier,
                UUID.randomUUID().toString(),
                correctedStudent.birthDate,
                false
        )
    }

    private fun saveCorrectedAndRemoveStudentInsertError(corrected: Student, toRemove: StudentInsertError): Student {
        val savedAfterCorrection: Student = studentRepository.save(corrected)
        log.info("Saved corrected student: {}", savedAfterCorrection)
        log.info("To remove student: {}", toRemove)
        studentInsertErrorRepository.delete(toRemove)
        return savedAfterCorrection
    }

    fun getInputErrorStudents(): List<StudentDTO> {
        return studentInsertErrorRepository.findAll().map { StudentDTO(it) }.toList()
    }

    fun delete(studentId: Long) {
        log.info("Deleting insert error student records with id $studentId")
        studentInsertErrorRepository.deleteById(studentId)
        log.info("Deleting insert error student records with id $studentId DONE")
    }

    fun getInputErrorStudent(studentId: Long): StudentDTO {
        return studentInsertErrorRepository.findById(studentId).takeIf { it.isPresent }?.get()?.let { StudentDTO(it) }
                ?: throw IllegalArgumentException("Insert error student record with it $studentId not found")
    }
}