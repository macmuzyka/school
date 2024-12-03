package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.controller.FrontendNotificationSenderService
import com.school.model.OptionalRequestParams
import com.school.model.dto.GradeDTO
import com.school.model.dto.StudentSubjectGradesDTO
import com.school.model.entity.Grade
import com.school.repository.GradeRepository
import com.school.repository.StudentRepository
import com.school.repository.SubjectRepository
import com.school.service.utils.mapper.QueryResultsMappingUtils.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GradeService(
        private val subjectRepository: SubjectRepository,
        private val studentRepository: StudentRepository,
        private val frontendNotificationSenderService: FrontendNotificationSenderService,
        private val gradeRepository: GradeRepository,
        private val environmentService: EnvironmentService,
        private val applicationConfig: ApplicationConfig
) {

    private val log = LoggerFactory.getLogger(GradeService::class.java)
    //TODO: consider moving notifying frontend via web-socket from GradeConsumingByKafkaService to this class
    fun addGrade(grade: GradeDTO): Grade? {
        return try {
            return gradeRepository.save(buildGradeWithCoupledEntityObjects(grade)).also {
                if (!environmentService.currentProfileOtherThanDevel()) {
                    frontendNotificationSenderService.notifyFrontendAboutGradeMessageConsumed("OK")
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun buildGradeWithCoupledEntityObjects(grade: GradeDTO): Grade {
        return try {
            Grade().apply {
                gradeValue = validateGrade(grade.value)
                student = findStudentFromPassedGrade(grade.studentId)
                subject = findSubjectFromPassedGrade(grade.subjectId)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }
    }

    private fun validateGrade(gradeValue: Int) = gradeValue.takeIf { applicationConfig.availableGrades.contains(gradeValue) }
            ?: throw IllegalArgumentException("Grade value to add is not of available range")

    private fun findStudentFromPassedGrade(studentId: Long) = studentRepository.findById(studentId).takeIf { it.isPresent }?.get()
            ?: throw IllegalArgumentException("Cannot fund subject with id $studentId")

    private fun findSubjectFromPassedGrade(subjectId: Long) = subjectRepository.findById(subjectId).takeIf { it.isPresent }?.get()
            ?: throw IllegalArgumentException("Cannot fund subject with id $subjectId")

    fun getSubjectGradesForStudents(params: OptionalRequestParams): List<StudentSubjectGradesDTO> {
        log.info("Params: {}", params)
        val queryResults = gradeRepository.findAllGradesGroupedBySubject(
                params.id,
                params.subject,
                params.name,
                params.surname,
                params.identifier
        )

        try {
            return queryResults
                    .map { result -> buildStudentSubjectGradesObject(result) }
                    .toList()
                    .takeIf { it.isNotEmpty() } ?: getEmptyGradesListIndicator()
        } catch (e: Exception) {
            log.error(e.message)
            log.error(e.stackTrace.contentToString())
            throw e
        }
    }

    fun getSubjectGradesForStudent(studentId: Long) = gradeRepository.findAllGradesGroupedBySubjectForSingleStudent(studentId)
            .map { result -> prepareGradesForSingleStudent(result) }
            .toList()
}