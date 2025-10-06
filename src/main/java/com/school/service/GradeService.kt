package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.model.OptionalRequestParams
import com.school.model.SubjectsWithGrades
import com.school.model.dto.GradeDTO
import com.school.model.dto.StudentSubjectGradesDTO
import com.school.model.entity.Grade
import com.school.repository.sql.GradeRepository
import com.school.repository.sql.StudentRepository
import com.school.repository.sql.SubjectRepository
import com.school.service.utils.mapper.QueryResultsMapper.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GradeService(
    private val subjectRepository: SubjectRepository,
    private val studentRepository: StudentRepository,
    private val sendNotificationToFrontendService: SendNotificationToFrontendService,
    private val gradeRepository: GradeRepository,
    private val applicationConfig: ApplicationConfig
) {

    private val log = LoggerFactory.getLogger(GradeService::class.java)

    fun addGrade(grade: GradeDTO): GradeDTO? {
        return try {
            log.info("GradeDTO to be saved: $grade")
            GradeDTO(gradeRepository.save(buildGradeWithCoupledEntityObjects(grade)))
                    .also { sendNotificationAboutGradeInsertStatus("OK") }
        } catch (e: Exception) {
            sendNotificationAboutGradeInsertStatus(e.message ?: "No grade added")
            log.error(e.message)
            throw RuntimeException(e)
        }
    }

    private fun buildGradeWithCoupledEntityObjects(grade: GradeDTO): Grade {
        return try {
            Grade().apply {
                gradeValue = validateGrade(grade.value)
                student = findStudentFromPassedGrade(grade.studentId)
                subject = findSubjectFromPassedGrade(grade.subjectId)
                note = grade.note
                gradeType = grade.gradeType
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

    private fun sendNotificationAboutGradeInsertStatus(message: String) {
        sendNotificationToFrontendService.notifyFrontendAboutGradeConsumptionStatus(message)
    }

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

    fun getSubjectGradesForStudent(studentId: Long): List<SubjectsWithGrades> {
        return gradeRepository.findAllGradesGroupedBySubjectForSingleStudent(studentId)
                .map { result -> prepareGradesForSingleStudent(result) }
                .toList()
    }

    fun getGradeDetails(id: Long): GradeDTO {
        return gradeRepository.findById(id).also { log.info("Fetching grade details...") }
                .takeIf { it.isPresent }
                ?.get().also { log.info("Grade details found $it") }
                ?.let { GradeDTO(it) }
                ?: throw IllegalArgumentException("Could not find grade with ID $id")
    }
}