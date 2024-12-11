package com.school.model

import java.io.File

abstract class RecentlyProducedFileChecker {

    fun checkIfFileExists(directory: File) {
        if (!directory.exists() || !directory.isDirectory) {
            throw RuntimeException("Directory does not exist or is not a directory!")
        }
    }

    fun getFileIfExists(directory: File): File {
        return directory.listFiles().takeIf { it != null && it.isNotEmpty() }?.first()
                ?: throw RuntimeException("No files present in ${directory.name} or directory does not exist")

    }
}