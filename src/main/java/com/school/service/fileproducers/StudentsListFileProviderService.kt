package com.school.service.fileproducers

import com.school.configuration.FileConfig
import com.school.model.FileToImport
import com.school.model.OptionalRequestParams
import com.school.model.dto.StudentForListDTO
import com.school.model.dto.StudentForListDTO.Companion.asSingleRow
import com.school.repository.StudentRepository
import com.school.service.fileproducers.FileUtils.Companion.purifyDirectoryFromAllPreviousFiles
import com.school.service.fileproducers.FileUtils.Companion.validateAndPrepareFile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class StudentsListFileProviderService(
        private val studentRepository: StudentRepository,
        private val fileConfig: FileConfig
) : RecentlyProducedFile {
    private val log = LoggerFactory.getLogger(StudentsListFileProviderService::class.java)

    override fun importFile(params: OptionalRequestParams?): FileToImport {
        val directoryPath = fileConfig.directory + File.separator + fileConfig.studentsListSubdirectory
        val directory = File(directoryPath)
        purifyDirectoryFromAllPreviousFiles(directory)
        produceFileAndSaveToCorrespondingDirectory(directory.path, null)
        return prepareFileToImport(directory)
    }

    override fun produceFileAndSaveToCorrespondingDirectory(directoryPath: String, params: OptionalRequestParams?) {
        return createStudentListFile(directoryPath)
    }

    private fun createStudentListFile(directoryPath: String) {
        try {
            val forListRecords = studentRepository.findAll().map { StudentForListDTO(it) }
            val file = File(directoryPath + File.separator + "students-list_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".txt")
            BufferedWriter(FileWriter(file))
                    .use { writer ->
                        forListRecords.forEach { record ->
                            writer.write(record.asSingleRow())
                            writer.newLine()
                        }
                    }
        } catch (e: Exception) {
            log.error(e.message)
            e.printStackTrace()
            throw ErrorCreatingFileException(e.message ?: "Error creating file")
        }
    }

    private fun prepareFileToImport(directory: File): FileToImport {
        return validateAndPrepareFile(directory)
    }
}