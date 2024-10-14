package com.school.service;

import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.model.response.FileProviderResponse;
import com.school.repository.GradeRepository;
import com.school.service.utils.filetype.PreparationStrategy;
import com.school.service.utils.QueryResultsMappingUtils;
import com.school.model.dto.SubjectGradesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileProviderService {
    @Value("${files.destination.output.path}")
    private String filesDestinationPath;
    private final GradeRepository gradeRepository;

    public FileProviderService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public FileProviderResponse getProperFile(FileType fileType) throws IllegalAccessException {
        FileBuilder fileBuilder = PreparationStrategy.resolve(filesDestinationPath, fileType);
        return fileBuilder.prepare(getDataTransferObjects());
    }

    public List<SubjectGradesDTO> getDataTransferObjects() {
        return gradeRepository.findAllGradesGroupedBySubject(null).stream()
                .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                .toList();
    }
}
