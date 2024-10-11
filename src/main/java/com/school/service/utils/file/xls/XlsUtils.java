package com.school.service.utils.file.xls;

import com.school.model.SubjectGradesDTO;
import com.school.service.FileProviderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XlsUtils {
    private static final Logger log = LoggerFactory.getLogger(XlsUtils.class);

    public static String prepareXlsFile(List<SubjectGradesDTO> dataTransferObjects, String outputFilePath) {
        String resultMessage;
        try (Workbook workbook = new HSSFWorkbook();
             FileOutputStream outFile = new FileOutputStream(outputFilePath)) {

            Sheet sheet = workbook.createSheet("Grades");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Student Name");
            headerRow.createCell(1).setCellValue("Subject");
            headerRow.createCell(2).setCellValue("Grades");
            headerRow.createCell(3).setCellValue("Average Grade");

            int currentRow = 1;
            for (SubjectGradesDTO record : dataTransferObjects) {
                Row row = sheet.createRow(currentRow++);
                row.createCell(0).setCellValue(record.getStudentName());
                row.createCell(1).setCellValue(record.getSubject());
                row.createCell(2).setCellValue(record.getGrades());
                row.createCell(3).setCellValue(record.getAverageGrade().doubleValue());
            }

            workbook.write(outFile);
            resultMessage = "Xls file created successfully.";
            log.info(resultMessage);
            return resultMessage;

        } catch (IOException e) {
            resultMessage = "prepareCsvFile() -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return resultMessage;
        }
    }
}
