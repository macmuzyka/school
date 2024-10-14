package com.school.service.utils.filetype.xls;

import com.school.model.*;
import com.school.model.dto.SubjectGradesDTO;
import com.school.model.response.FileProviderResponse;
import com.school.model.response.FileStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XlsUtils extends BaseFile implements FileBuilder {
    private static final Logger log = LoggerFactory.getLogger(XlsUtils.class);

    public XlsUtils(String outputPath, String fileExtension) {
        super(outputPath, fileExtension);
    }

    @Override
    public FileProviderResponse prepare(List<SubjectGradesDTO> records) {
        String resultMessage;
        try (Workbook workbook = new HSSFWorkbook();
             FileOutputStream outFile = new FileOutputStream(this.getOutputPath() + this.getFileExtension())) {

            Sheet sheet = workbook.createSheet("Grades");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Student Name");
            headerRow.createCell(1).setCellValue("Subject");
            headerRow.createCell(2).setCellValue("Grades");
            headerRow.createCell(3).setCellValue("Average Grade");

            int currentRow = 1;
            for (SubjectGradesDTO record : records) {
                Row row = sheet.createRow(currentRow++);
                row.createCell(0).setCellValue(record.getStudentName());
                row.createCell(1).setCellValue(record.getSubject());
                row.createCell(2).setCellValue(record.getGrades());
                row.createCell(3).setCellValue(record.getAverageGrade().doubleValue());
            }

            workbook.write(outFile);
            resultMessage = "Xls file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, records.size(), resultMessage);

        } catch (IOException e) {
            resultMessage = "prepareCsvFile() -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
