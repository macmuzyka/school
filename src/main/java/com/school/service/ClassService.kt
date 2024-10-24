package com.school.service

import com.school.repository.SchoolClassRepository
import com.school.repository.StudentRepository
import com.school.service.utils.mapper.QueryResultsMappingUtils
import com.schoolmodel.model.dto.ClassWithStudentsDTO
import com.schoolmodel.model.dto.ExistingStudentToClassDTO
import com.schoolmodel.model.dto.SimpleClassDTO
import com.schoolmodel.model.entity.SchoolClass
import com.schoolmodel.model.entity.Student
import com.schoolmodel.model.enums.ClassAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassService(private val studentRepository: StudentRepository,
                   private val schoolClassRepository: SchoolClassRepository) {
    private val log: Logger = LoggerFactory.getLogger(ClassService::class.java)
    fun getClassesWithStudents(): List<ClassWithStudentsDTO> {
        return try {
            schoolClassRepository.findStudentsGroupedIntoClasses()
                    .map { QueryResultsMappingUtils.buildClassWithStudentsObject(it) }
                    .toList()
        } catch (e: Exception) {
            log.error(e.message)
            log.error(e.stackTrace.contentToString())
            throw e
        }
    }

    fun studentToClassAction(existingStudentToClassDTO: ExistingStudentToClassDTO): SimpleClassDTO {
        val (studentCode, schoolClassId, action) = existingStudentToClassDTO

        val foundStudent = studentRepository.findStudentByCode(studentCode).takeIf { it.isPresent }?.get()
                ?: throw IllegalArgumentException("Student with code: $studentCode not found!")
        val foundSchoolClass = schoolClassRepository.findById(schoolClassId).takeIf { it.isPresent }?.get()
                ?: throw IllegalArgumentException("School class with ID: $schoolClassId not found!")

        return when (action) {
            ClassAction.ADD -> assignStudentToClass(foundStudent, foundSchoolClass)
            ClassAction.MOVE -> moveStudentToOtherClass(foundStudent, foundSchoolClass)
            ClassAction.REMOVE -> removeStudentFromClass(foundStudent, foundSchoolClass)
            else -> {
                throw IllegalArgumentException("Action $action not supported!")
            }
        }
    }

    private fun assignStudentToClass(student: Student, schoolClass: SchoolClass): SimpleClassDTO {
        if (isAssigned(student, schoolClass)) {
            throw IllegalArgumentException("Student $student is already assigned to class!")
        } else {
            student.isAssigned = true
            schoolClass.classStudents.add(student)
            log.info("[ASSIGN] Student [${student.simpleDisplay()}] assigned to class: [${schoolClass.name}]")
            val updatedClass = schoolClassRepository.save(schoolClass)
            log.debug("[ASSIGN] Updated class: {}", updatedClass.simpleDisplay())
            return SimpleClassDTO(updatedClass)
        }
    }

    private fun moveStudentToOtherClass(student: Student, destinationClass: SchoolClass): SimpleClassDTO {
        if (isAssigned(student, destinationClass)) {
            throw IllegalArgumentException("Attempting to move student $student to the same class!")
        } else {
            destinationClass.classStudents.add(student)
            val updatedClass = schoolClassRepository.save(destinationClass)
            log.debug("[MOVE] Updated class: {}", updatedClass.simpleDisplay())
            return SimpleClassDTO(updatedClass)
        }
    }

    private fun removeStudentFromClass(student: Student, schoolClass: SchoolClass): SimpleClassDTO {
        student.isAssigned = false
        val removed = schoolClass.classStudents.remove(student)
        if (removed) {
            log.info("[REMOVE] Student [${student.simpleDisplay()}] removed from class: [${schoolClass.name}]")
        } else {
            log.error("Error removing student with code ${student.code}",)
        }

        val updatedClass = schoolClassRepository.save(schoolClass)
        log.debug("[REMOVE] Updated class: ${updatedClass.simpleDisplay()}")
        return SimpleClassDTO(updatedClass)
    }

    private fun isAssigned(student: Student, schoolClass: SchoolClass): Boolean {
        return schoolClass.classStudents.contains(student)
    }
}