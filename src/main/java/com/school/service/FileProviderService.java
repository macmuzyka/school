package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.model.response.FileProviderResponse;
import com.school.repository.GradeRepository;
import com.school.service.utils.QueryResultsMappingUtils;
import com.school.model.dto.SubjectGradesDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FileProviderService {
    private final GradeRepository gradeRepository;
    private final FileConfig fileConfig;

    public FileProviderService(GradeRepository gradeRepository, FileConfig fileConfig) {
        this.gradeRepository = gradeRepository;
        this.fileConfig = fileConfig;
    }

    public FileProviderResponse getProperFile(String fileType) throws IllegalAccessException {
        FileType resolvedFileType;
        try {
            resolvedFileType = FileType.valueOf(fileType.toUpperCase());
        } catch (Exception e) {
            List<String> values = Arrays.stream(FileType.values()).map(it -> it.toString()).toList();
            throw new IllegalAccessException("Declared file type [" + fileType + "] not supported yet! Available types are: " + values);
        }
        FileBuilder fileBuilder = PreparationStrategy.resolve(resolvedFileType, fileConfig);
        return fileBuilder.prepare(getDataTransferObjects());
    }

    public List<SubjectGradesDTO> getDataTransferObjects() {
        return gradeRepository.findAllGradesGroupedBySubject(null).stream()
                .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                .toList();
    }
}
