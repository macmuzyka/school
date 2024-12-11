package com.school.service

import com.school.configuration.FileConfig
import com.school.model.FileToImport
import com.school.model.RecentlyProducedFile
import com.school.model.RecentlyProducedFileChecker
import com.school.model.dto.StudentForListDTO
import com.school.model.dto.StudentForListDTO.Companion.asSingleRow
import com.school.repository.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalDate

@Service
class StudentsListFileProviderService(
        private val studentRepository: StudentRepository,
        private val directoryPurifierService: DirectoryPurifierService,
        private val fileConfig: FileConfig
) : RecentlyProducedFileChecker(), RecentlyProducedFile {
    private val log = LoggerFactory.getLogger(StudentsListFileProviderService::class.java)
    fun getFile(): FileToImport {
        val directoryPath = fileConfig.directory + fileConfig.studentsListSubdirectory
        val directory = File(directoryPath)
        directoryPurifierService.purifyDirectoryFromAllPreviousFiles(directory)
        if(createStudentListFile(directoryPath)) {
            return getRecentlyProducedFile(File(directoryPath)) ?: throw RuntimeException("Error getting recently produced file")
        } else {
            throw RuntimeException("Error producing student list file")
        }
    }

    private fun createStudentListFile(directoryPath: String): Boolean {
        try {
            val forListRecords = studentRepository.findAll().map { StudentForListDTO(it) }.also {
                log.info("STUDENTS FOUND: ${it.size}")
                log.info("STUDENTS FOUND: ${it.size}")
                log.info("STUDENTS FOUND: ${it.size}")
                log.info("STUDENTS FOUND: ${it.size}")
                log.info("STUDENTS FOUND: ${it.size}")
                log.info("STUDENTS FOUND: ${it.size}")
            }
            val file = File(directoryPath + "students-list_" + LocalDate.now() + ".txt")
            BufferedWriter(FileWriter(file))
                    .use { writer ->
                        forListRecords.forEach { record ->
                            writer.write(record.asSingleRow())
                            writer.newLine()
                        }
                    }
            return true
        } catch (e: Exception) {
            log.error(e.message)
            e.printStackTrace()
            return false
        }

    }

    override fun getRecentlyProducedFile(directory: File?): FileToImport? {
        checkIfFileExists(directory!!)
        val file = getFileIfExists(directory)
        try {
            val fsr = FileSystemResource(file)
            log.info("File to return name: ${file.name}")
            return FileToImport(fsr, file.name)
        } catch (e: Exception) {
            throw RuntimeException("Error getting file from directory $directory")
        }
    }
}