package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.repository.SchoolClassRepository
import com.school.repository.SchoolRepository
import com.school.repository.StudentRepository
import com.school.repository.SubjectRepository
import com.school.service.utils.mapper.QueryResultsMappingUtils
import com.school.model.dto.*
import com.school.model.entity.SchoolClass
import com.school.model.entity.Student
import com.school.model.entity.Subject
import com.school.model.enums.ClassAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassService(
        private val studentRepository: StudentRepository,
        private val schoolClassRepository: SchoolClassRepository,
        private val subjectRepository: SubjectRepository,
        private val schoolRepository: SchoolRepository,
        private val applicationConfig: ApplicationConfig,
) {
    private val log: Logger = LoggerFactory.getLogger(ClassService::class.java)

    fun getClassesWithStudents(): List<ClassWithListedStudentsDTO> {
        return try {
            schoolClassRepository.findListedStudentsGroupedIntoClasses()
                    .map { QueryResultsMappingUtils.buildClassWithListedStudents(it) }
                    .toList()
        } catch (e: Exception) {
            log.error(e.message)
            log.error(e.stackTrace.contentToString())
            throw e
        }
    }

    fun studentToClassAction(existingStudentToClassDTO: ExistingStudentToClassDTO): ClassDTO {
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

    private fun assignStudentToClass(student: Student, schoolClass: SchoolClass): ClassDTO {
        if (isAssigned(student, schoolClass)) {
            throw IllegalArgumentException("Student $student is already assigned to class!")
        } else {
            student.isAssigned = true
            schoolClass.classStudents.add(student)
            log.info("[ASSIGN] Student [${student.simpleDisplay()}] assigned to class: [${schoolClass.name}]")
            val updatedClass = schoolClassRepository.save(schoolClass)
            log.debug("[ASSIGN] Updated class: {}", updatedClass.simpleDisplay())
            return ClassDTO(updatedClass)
        }
    }

    private fun moveStudentToOtherClass(student: Student, destinationClass: SchoolClass): ClassDTO {
        if (isAssigned(student, destinationClass)) {
            throw IllegalArgumentException("Attempting to move student $student to the same class!")
        } else {
            destinationClass.classStudents.add(student)
            val updatedClass = schoolClassRepository.save(destinationClass)
            log.debug("[MOVE] Updated class: {}", updatedClass.simpleDisplay())
            return ClassDTO(updatedClass)
        }
    }

    private fun removeStudentFromClass(student: Student, schoolClass: SchoolClass): ClassDTO {
        student.isAssigned = false
        val removed = schoolClass.classStudents.remove(student)
        if (removed) {
            log.info("[REMOVE] Student [${student.simpleDisplay()}] removed from class: [${schoolClass.name}]")
        } else {
            log.error("Error removing student with code ${student.code}")
        }

        val updatedClass = schoolClassRepository.save(schoolClass)
        log.debug("[REMOVE] Updated class: ${updatedClass.simpleDisplay()}")
        return ClassDTO(updatedClass)
    }

    private fun isAssigned(student: Student, schoolClass: SchoolClass): Boolean {
        return schoolClass.classStudents.contains(student)
    }

    private fun createNewClass(): SchoolClass {
        val resolvedNextClassNumber = findNextClassNumber()
        val newSchoolClass = schoolClassRepository.save(
                SchoolClass("Class $resolvedNextClassNumber", applicationConfig.availableSubjects
                        .map { subject -> subjectRepository.save(Subject(subject)) }
                        .toList()
                )
        )

        val school = schoolRepository.findById(1L).takeIf { it.isPresent }?.get()
                ?: throw IllegalStateException("No school to assign class to! This should be done upon application warmup!")
        school.schoolClasses.add(newSchoolClass)
        schoolRepository.save(school)
        return newSchoolClass
    }

    private fun findNextClassNumber(): Int {
        val maxClassNumber = schoolClassRepository.findAll()
                .map { schoolClass -> Integer.valueOf(schoolClass.name.substringAfter(" ")) }
                .maxOf { it }
        return maxClassNumber + 1
    }

    fun createClass(newClassDto: NewClassDTO): SchoolClass = schoolClassRepository.save(
            SchoolClass(
                    newClassDto.name,
                    applicationConfig.availableSubjects
                            .map { sub -> subjectRepository.save(Subject(sub)) }
                            .toList()
            )
    )

    fun assignStudentToFirstOpenClass(student: Student?): SchoolClass {
        val openClassId = schoolClassRepository.findSchoolClassesIdsWithStudentCountLessThanMaxClassSize(applicationConfig.classMaxSize).firstOrNull()
        val foundOpenClass = openClassId?.let { schoolClassRepository.findById(it).orElse(null) } ?: createNewClass()
        return student?.let { foundOpenClass.apply { classStudents.add(student) }.also { student.isAssigned = true } } ?: foundOpenClass
    }
}