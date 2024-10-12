package com.school.model;

import com.school.model.dto.SubjectGradesDTO;
import com.school.model.response.FileProviderResponse;

import java.util.List;

public interface FilePreparation {
    FileProviderResponse prepareFile(List<SubjectGradesDTO> dataTransferObjects, String outputFilePath);
}
