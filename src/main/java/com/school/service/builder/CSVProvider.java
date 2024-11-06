package com.school.service.builder;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.school.configuration.FileConfig;
import com.school.model.FileResource;
import com.schoolmodel.model.response.FileProviderResponse;
import com.schoolmodel.model.enums.FileStatus;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
import com.school.model.FileProvider;
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
        try (FileWriter writer = new FileWriter(this.getFullPathWithoutExtension())) {
            StatefulBeanToCsv<StudentSubjectGradesDTO> beanToCsv =
                    new StatefulBeanToCsvBuilder<StudentSubjectGradesDTO>(writer)
                            .withQuotechar('\'')
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();

            beanToCsv.write(records);

            resultMessage = "CSV file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, records.size(), resultMessage);
        } catch (Exception e) {
            resultMessage = "CSV prepare -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
