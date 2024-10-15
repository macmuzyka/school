package com.school.model;

import com.school.model.dto.SubjectGradesDTO;
import com.school.model.response.FileProviderResponse;

import java.util.List;

public interface FileBuilder {
    FileProviderResponse prepare(List<SubjectGradesDTO> dataTransferObjects);
}
