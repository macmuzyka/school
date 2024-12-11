package com.school.service.fileproducers

import com.school.model.FileToImport
import com.school.model.OptionalRequestParams

interface RecentlyProducedFile {
    fun importFile(params: OptionalRequestParams?): FileToImport
    fun produceFileAndSaveToCorrespondingDirectory(directoryPath: String, params: OptionalRequestParams?)
}