package com.school.service;

import com.school.model.request.FileType;
import com.school.model.response.FileProviderResponse;
import com.school.repository.GradeRepository;
import com.school.service.utils.filetype.csv.CsvUtils;
import com.school.service.utils.QueryResultsMappingUtils;
import com.school.model.dto.SubjectGradesDTO;
import com.school.service.utils.filetype.xls.XlsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileProviderService {
    private static final Logger log = LoggerFactory.getLogger(FileProviderService.class);
    private final GradeRepository gradeRepository;
    @Value("${files.output.path}")
    private String outputFilePath;
    private final String csvExtension = ".csv";
    private final String xlsExtension = ".xls";

    public FileProviderService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public FileProviderResponse getProperFile(FileType fileType) throws IllegalAccessException {
        switch (fileType) {
            case CSV, csv -> {
                return new CsvUtils().prepareFile(getDataTransferObjects(), outputFilePath + csvExtension);
            }
            case XLS, xls -> {
                return new XlsUtils().prepareFile(getDataTransferObjects(), outputFilePath + xlsExtension);
            }
            default -> throw new IllegalAccessException("FileType: " + fileType + " not supported yet!");
        }
    }

    public List<SubjectGradesDTO> getDataTransferObjects() {
        return gradeRepository.findAllGradesGroupedBySubject(null).stream()
                .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                .toList();
    }
}
