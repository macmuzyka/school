package com.school.service.builder;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.school.configuration.FileConfig;
import com.school.model.FileResource;
import com.school.model.FileProvider;
import com.school.model.dto.StudentSubjectGradesCsvBeanDTO;
import com.school.model.dto.StudentSubjectGradesDTO;
import com.school.model.enums.FileStatus;
import com.school.model.response.FileProviderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.List;


public class CSVProvider extends FileResource implements FileProvider {
    private static final Logger log = LoggerFactory.getLogger(CSVProvider.class);

    public CSVProvider(String fileExtension, FileConfig fileConfig) {
        super(fileExtension, fileConfig);
    }

    @Override
    public FileProviderResponse build(List<StudentSubjectGradesDTO> records) {
        String resultMessage;
        List<StudentSubjectGradesCsvBeanDTO> recordsAsCsvBeans = records.stream().map(StudentSubjectGradesCsvBeanDTO::new).toList();
        try (FileWriter writer = new FileWriter(this.getFullPathWithoutExtension())) {
            StatefulBeanToCsv<StudentSubjectGradesCsvBeanDTO> beanToCsv =
                    new StatefulBeanToCsvBuilder<StudentSubjectGradesCsvBeanDTO>(writer)
                            .withQuotechar('\'')
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();

            beanToCsv.write(recordsAsCsvBeans);

            resultMessage = "CSV file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, recordsAsCsvBeans.size(), resultMessage);
        } catch (Exception e) {
            resultMessage = "CSV prepare -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
