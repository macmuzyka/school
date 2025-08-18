package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.repository.SchoolClassRepository
import com.school.repository.SchoolRepository
import com.school.repository.StudentRepository
import com.school.service.utils.mapper.QueryResultsMapper
import com.school.model.dto.*
import com.school.model.entity.School
import com.school.model.entity.SchoolClass
import com.school.model.entity.Student
import com.school.model.entity.Subject
import com.school.model.entity.classschedule.ClassSchedule
import com.school.model.enums.ClassAction
import com.school.service.classschedule.EmptyScheduleSchemaBuilderService
import com.school.service.utils.EntityFetcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
//TODO: refactor this shit
class SchoolClassService(
    private val studentRepository: StudentRepository,
    private val schoolClassRepository: SchoolClassRepository,
    private val schoolRepository: SchoolRepository,
    private val emptyScheduleSchemaBuilderService: EmptyScheduleSchemaBuilderService,
    private val applicationConfig: ApplicationConfig,
) {
    private val log: Logger = LoggerFactory.getLogger(SchoolClassService::class.java)

    fun getClassesWithStudents(): List<ClassWithListedStudentsDTO> {
        return try {
            schoolClassRepository.findListedStudentsGroupedIntoClasses()
                .map { QueryResultsMapper.buildClassWithListedStudents(it) }
                .toList()
        } catch (e: Exception) {
            log.error(e.message)
            log.error(e.stackTrace.contentToString())
            throw e
        }
    }

    fun getClassesDTOs(): List<ClassForScheduleDTO> {
        return schoolClassRepository.findAll()
            .map { schoolClass ->
                ClassForScheduleDTO(
                    schoolClass.id,
                    schoolClass.name,
                    schoolClass.classSchedule != null
                )
            }.toList()
    }


    //TODO: separate each action to each controller method
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

    private fun isAssigned(student: Student, schoolClass: SchoolClass): Boolean {
        return schoolClass.classStudents.contains(student)
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

    fun assignStudentToFirstOpenClass(student: Student): SchoolClass {
        val openClassId = schoolClassRepository
            .findSchoolClassesIdsWithStudentCountLessThanMaxClassSize(applicationConfig.classMaxSize)
            .firstOrNull()
            .also { log.debug("Open class id found: $it") }

        val openClassFound = openClassId?.let { openClassIdFound ->
            schoolClassRepository
                .findById(openClassIdFound).orElse(null)
                .also { log.info("Open class found: ${it.name}") }
        } ?: createNewClass()
            .also { log.info("Needed to create new class: ${it.name}") }

        return openClassFound.apply {
            classStudents.add(student)
            student.isAssigned = true
            student.schoolClass = this
        }
    }

    fun createNewClass(): SchoolClass {
        val newSchoolClass = createNewClassWithAssignedSubjects()
        emptyScheduleSchemaBuilderService.generateEmptySchedule(newSchoolClass)

        val school = schoolRepository.findAll()
            .firstOrNull()
            ?: throw IllegalStateException("No school to assign class to! This should be done upon application warmup!")
        school.schoolClasses.add(newSchoolClass)
        schoolRepository.save(school)

        return newSchoolClass
    }

    fun createNewClassWithAssignedSubjects(): SchoolClass {
        val newSchoolClass =
            SchoolClass("Class ${findNextClassNumber()}")
        val newClassSubjects = applicationConfig.availableSubjects
            .map { subject -> Subject(subject, newSchoolClass) }
            .toSet()
        newSchoolClass.classSubjects = newClassSubjects
        newSchoolClass.setSchool(motherSchool())
        return schoolClassRepository.save(newSchoolClass)
    }

    private fun findNextClassNumber(): Int {
        val nextClassNumber = schoolClassRepository.findAll()
            .takeIf { it.isNotEmpty() }
            ?.map { schoolClass -> Integer.valueOf(schoolClass.name.substringAfter(" ")) }
            ?.maxOf { it + 1 }
            ?.also { log.debug("Next class number resolved: $it") } ?: 1
        return nextClassNumber
    }

    private fun motherSchool(): School {
        return schoolRepository.findAll()[0]
    }

    fun getSchoolClassByClassSchedule(schedule: ClassSchedule): SchoolClass =
        EntityFetcher.getByIdOrThrow(schoolClassRepository::findById, schedule.id, "SchoolClass")

    fun getSchoolClass(schoolClassId: Long) = EntityFetcher.getByIdOrThrow(schoolClassRepository::findById, schoolClassId, "SchoolClass")
}