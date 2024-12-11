package com.school.model

import java.io.File

interface RecentlyProducedFile {
    fun getRecentlyProducedFile(directory: File?): FileToImport?
}