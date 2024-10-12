package com.school.service.utils.filetype.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.school.model.response.FileProviderResponse;
import com.school.model.response.FileStatus;
import com.school.model.dto.SubjectGradesDTO;
import com.school.model.FilePreparation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.List;


public class CsvUtils implements FilePreparation {
    private static final Logger log = LoggerFactory.getLogger(CsvUtils.class);

    @Override
    public FileProviderResponse prepareFile(List<SubjectGradesDTO> records, String outputFilePath) {
        String resultMessage;
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            StatefulBeanToCsv<SubjectGradesDTO> beanToCsv =
                    new StatefulBeanToCsvBuilder<SubjectGradesDTO>(writer)
                            .withQuotechar('\'')
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();

            beanToCsv.write(records);

            resultMessage = "CSV file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, records.size(), resultMessage);
        } catch (Exception e) {
            resultMessage = "prepareCsvFile() -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
