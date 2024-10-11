package com.school.service.utils.file.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.school.model.SubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.List;


public class CsvUtils {
    private static final Logger log = LoggerFactory.getLogger(CsvUtils.class);

    public static String prepareCsvFile(List<SubjectGradesDTO> records, String outputFilePath) {
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
            return resultMessage;
        } catch (Exception e) {
            resultMessage = "prepareCsvFile() -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return resultMessage;
        }
    }
}
