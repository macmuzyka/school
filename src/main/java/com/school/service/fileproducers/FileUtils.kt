package com.school.service.fileproducers

import com.school.model.FileToImport
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import java.io.File

class FileUtils {
    companion object {
        private val log = LoggerFactory.getLogger(FileUtils::class.java)

        @JvmStatic
        fun validateAndPrepareFile(directory: File): FileToImport {
            checkIfFileExists(directory);
            val file = getFileIfExists(directory);
            try {
                val fileSystemResource = FileSystemResource(file)
                return FileToImport(fileSystemResource, file.name)
            } catch (e: Exception) {
                throw RuntimeException("Error getting file from directory $directory")
            }
        }

        private fun checkIfFileExists(directory: File) {
            if (!directory.exists() || !directory.isDirectory) {
                throw RuntimeException("Directory does not exist or is not a directory!")
            }
        }

        private fun getFileIfExists(directory: File): File {
            return directory.listFiles().takeIf { it != null && it.isNotEmpty() }?.first()
                    ?: throw RuntimeException("No files present in ${directory.name} or directory does not exist")

        }

        @JvmStatic
        fun purifyDirectoryFromAllPreviousFiles(directory: File) {
            directory.takeIf { it.exists() && it.isDirectory }?.let {
                directory.listFiles()?.let { deleteAllPreviousFiles(it) }
                        ?: log.info("No files to delete from directory $directory")
            } ?: throw RuntimeException("Directory $directory does not exist")
        }

        private fun deleteAllPreviousFiles(files: Array<File>) {
            files.forEach { file -> file.takeIf { it.delete() }?.let { log.info("File ${file.name} deleted") } }
        }
    }
}