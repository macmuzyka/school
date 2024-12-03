package com.school.model;

import com.school.model.dto.StudentSubjectGradesDTO;
import com.school.model.response.FileProviderResponse;

import java.util.List;

public interface FileProvider {
    FileProviderResponse build(List<StudentSubjectGradesDTO> dataTransferObjects);
}
