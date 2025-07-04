package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.model.dto.StudentDTO
import com.school.model.entity.Student
import com.school.model.entity.StudentDuplicateError
import com.school.model.entity.StudentInsertError
import com.school.model.enums.InsertStatus
import com.school.repository.SchoolClassRepository
import com.school.repository.StudentDuplicateErrorRepository
import com.school.repository.StudentInsertErrorRepository
import com.school.repository.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class StudentsFromListBuilderService(
    private val studentRepository: StudentRepository,
    private val studentDuplicateErrorRepository: StudentDuplicateErrorRepository,
    private val studentInsertErrorRepository: StudentInsertErrorRepository,
    private val schoolClassRepository: SchoolClassRepository,
    private val classService: ClassService,
    private val sendNotificationToFrontendService: SendNotificationToFrontendService,
    private val applicationConfig: ApplicationConfig
) {
    private val log = LoggerFactory.getLogger(StudentsFromListBuilderService::class.java)
    private val inserts: MutableMap<InsertStatus, MutableSet<StudentDTO>> = mutableMapOf()
    private fun validInserts() = inserts[InsertStatus.SUCCESS] ?: mutableSetOf()
    private fun duplicatedInerts() = inserts[InsertStatus.DUPLICATED] ?: mutableSetOf()
    private fun errorInserts() = inserts[InsertStatus.ERROR] ?: mutableSetOf()

    fun saveStudentsFromFile(studentsFile: MultipartFile): String {
        initializeAuxiliaryMap()
        return try {
            //TODO: idea -> real all lines, then map to Map<K,V>, where key (K) is validation result for a line,
            // thus method is separated into reading & saving records,
            // making code more readable & probably another factory pattern
            studentsFile.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val tagsFound = line.split(" ")
                    tagsFound.takeIf { lineHasProperNumberOFTags(it) }
                        ?.let { values ->
                            buildAndSaveStudentFromReaderLineParts(values)
                                ?.let { studentBuilt -> assignClass(studentBuilt) }
                                ?: handleDuplicateStudentRecord(values)
                        } ?: buildAndSaveInsertErrorStudentRecord(tagsFound)
                }
            }
            "File uploaded successfully"
        } catch (e: Exception) {
            log.error(e.message)
            e.printStackTrace()
            "Error uploading students file: ${e.message}"
        } finally {
            clearAuxiliaryMap()
        }
    }

    private fun initializeAuxiliaryMap() {
        inserts[InsertStatus.SUCCESS] = studentRepository.findAll().map { StudentDTO(it) }.toMutableSet()
        inserts[InsertStatus.ERROR] = studentInsertErrorRepository.findAll().map { StudentDTO(it) }.toMutableSet()
        inserts[InsertStatus.DUPLICATED] =
            studentDuplicateErrorRepository.findAll().map { StudentDTO(it) }.toMutableSet()
    }

    private fun lineHasProperNumberOFTags(lineParts: List<String>) =
        lineParts.size == applicationConfig.minimumStudentTags

    private fun buildAndSaveStudentFromReaderLineParts(tags: List<String>): Student? {
        val toSave = buildStudentFromTags(tags)
        validInserts().add(StudentDTO(toSave)).let { validNewStudentRecord ->
            return if (validNewStudentRecord) {
                studentRepository.save(toSave)
            } else {
                null
            }
        }
    }

    private fun buildStudentFromTags(tags: List<String>): Student {
        return Student().apply {
            this.name = tags[1]
            this.surname = tags[2]
            this.birthDate = LocalDate.parse(tags[3], DateTimeFormatter.ISO_LOCAL_DATE)
            this.identifier = tags[4]
            this.code = UUID.randomUUID().toString()
            this.isAssigned = false
        }
    }

    private fun assignClass(student: Student) {
        val schoolClass = classService.assignStudentToFirstOpenClass(student)
        schoolClassRepository.save(schoolClass)
    }

    private fun handleDuplicateStudentRecord(values: List<String>) {
        val duplicate = StudentDuplicateError(
            buildStudentFromTags(values),
            "Duplicated Identifier",
            "Insert error"
        )
        duplicatedInerts().add(StudentDTO(duplicate)).let { newDuplicate ->
            if (newDuplicate) {
                studentDuplicateErrorRepository.save(duplicate)
                    .also { log.info("Student with identifier ${it.identifier} saved as duplicate") }
                    .also { sendNotificationToFrontendService.notifyFrontendAboutStudentDuplicateDetected("Student duplicate, identifier ${it.identifier} already exists, please verify") }
            } else {
                log.info("Duplicated identifier ${duplicate.identifier} already saved")
            }
        }
    }

    private fun buildAndSaveInsertErrorStudentRecord(invalidTags: List<String>) {
        val pseudoIdentifier = invalidTags.joinToString("&")
        val studentInsertError = StudentInsertError(
            Student(
                "",
                "",
                pseudoIdentifier,
                LocalDate.now(),
                false
            ),
            "Error line in file: $pseudoIdentifier",
            "Bad record format in student file"
        )
        errorInserts().add(StudentDTO(studentInsertError)).let { added ->
            if (added) {
                studentInsertErrorRepository.save(studentInsertError)
                    .also { log.info("Student insert error record saved ${StudentDTO(it)}") }
                    .also {
                        sendNotificationToFrontendService.notifyFrontendAboutStudentInsertErrorDetected(
                            "Error inserting student $invalidTags invalid number of tags, please verify "
                        )
                    }
            } else {
                log.info("Student insert error with pseudo identifier $pseudoIdentifier already saved")
            }
        }
    }

    private fun clearAuxiliaryMap() {
        inserts[InsertStatus.SUCCESS] = mutableSetOf()
        inserts[InsertStatus.ERROR] = mutableSetOf()
        inserts[InsertStatus.DUPLICATED] = mutableSetOf()
    }
}