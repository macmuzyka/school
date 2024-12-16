package com.school.service.utils.filetype;

import com.school.model.dto.StudentSubjectGradesDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public class XLSUtils {

    public static void prepareXLSDocument(List<StudentSubjectGradesDTO> records, Workbook workbook) {
        //TODO: add row numbers
        Sheet sheet = workbook.createSheet("Grades");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student Name");
        headerRow.createCell(1).setCellValue("Subject");
        headerRow.createCell(2).setCellValue("Grades");
        headerRow.createCell(3).setCellValue("Average Grade");

        int currentRow = 1;
        for (StudentSubjectGradesDTO record : records) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(record.getStudentName());
            row.createCell(1).setCellValue(record.getSubject());
            row.createCell(2).setCellValue((Double.valueOf(record.getExamGrades().toString())));
            row.createCell(3).setCellValue(Double.parseDouble(String.valueOf(record.getAverageGrade())));
        }
    }
}
