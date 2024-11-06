package com.school.model;

import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
import com.schoolmodel.model.response.FileProviderResponse;

import java.util.List;

public interface FileProvider {
    FileProviderResponse build(List<StudentSubjectGradesDTO> dataTransferObjects);
}
