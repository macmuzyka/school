package com.school.service

import com.school.repository.SchoolClassRepository
import com.school.repository.StudentRepository
import com.school.service.utils.mapper.QueryResultsMappingUtils
import com.schoolmodel.model.dto.ClassWithStudentsDTO
import com.schoolmodel.model.dto.ExistingStudentToClassDTO
import com.schoolmodel.model.dto.SimpleClassDTO
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

    fun assignStudentToClass(existingStudentToClassDTO: ExistingStudentToClassDTO): SimpleClassDTO {
        val (studentCode, schoolClassId) = existingStudentToClassDTO

        val foundStudent = studentRepository.findStudentByCode(studentCode).takeIf { it.isPresent }?.get()
                ?: throw IllegalArgumentException("Student with code: " + studentCode + "not found!")
        val foundSchoolClass = schoolClassRepository.findById(schoolClassId).takeIf { it.isPresent }?.get()
                ?: throw IllegalArgumentException("School class with ID: " + studentCode + "not found!")

        foundSchoolClass.classStudents.add(foundStudent)
        //FIXME: check if toString method works properly!
        log.info("Student [{}] assigned to class: [{}]", foundStudent.simpleDisplay(), foundSchoolClass.name)
        val updatedClass = schoolClassRepository.save(foundSchoolClass)
        log.debug("Updated class: {}", updatedClass.simpleDisplay())
        return SimpleClassDTO(updatedClass)
    }

    fun detachStudentFromClass(existingStudentFromClassDTO: ExistingStudentToClassDTO): SimpleClassDTO {
        val (studentCode, schoolClassId) = existingStudentFromClassDTO
        //TODO: implement
        throw NotImplementedError("Not yet implemented!")
    }
}