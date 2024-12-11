package com.school.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
class DirectoryPurifierService {
    private val log = LoggerFactory.getLogger(DirectoryPurifierService::class.java)

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