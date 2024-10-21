package com.school.model;

import com.schoolmodel.model.dto.SubjectGradesDTO;
import com.schoolmodel.model.response.FileProviderResponse;

import java.util.List;

public interface FileBuilder {
    FileProviderResponse prepare(List<SubjectGradesDTO> dataTransferObjects);
}
