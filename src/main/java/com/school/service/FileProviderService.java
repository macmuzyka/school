package com.school.service;

import com.school.repository.GradeRepository;
import com.school.service.utils.file.csv.CsvUtils;
import com.school.service.utils.QueryResultsMappingUtils;
import com.school.model.SubjectGradesDTO;
import com.school.service.utils.file.xls.XlsUtils;
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

    public String getCsvFile() {
        try {
            return CsvUtils.prepareCsvFile(getDataTransferObjects(), outputFilePath + csvExtension);
        } catch (Exception e) {
            String errorMessage = "getCsvFile() -> Error while creating CSV file from student subject grades records!";
            log.error(errorMessage);
            log.error(e.getMessage());
            return errorMessage;
        }
    }

    public String getXlsFile() {
        try {
            return XlsUtils.prepareXlsFile(getDataTransferObjects(), outputFilePath + xlsExtension);
        } catch (Exception e) {
            String errorMessage = "getXlsFile() -> Error while creating CSV file from student subject grades records!";
            log.error(errorMessage);
            log.error(e.getMessage());
            return errorMessage;
        }

    }

    public List<SubjectGradesDTO> getDataTransferObjects() {
        return gradeRepository.findAllGradesGroupedBySubject(null).stream()
                .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                .toList();
    }
}
