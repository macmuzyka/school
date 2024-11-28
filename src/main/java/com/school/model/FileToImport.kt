package com.school.model

import org.springframework.core.io.FileSystemResource

data class FileToImport(val fileToImport: FileSystemResource, val fileName: String)