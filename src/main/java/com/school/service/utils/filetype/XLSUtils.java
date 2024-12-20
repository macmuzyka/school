package com.school.service.utils.filetype;

import com.school.model.dto.GradeDisplayDTO;
import com.school.model.dto.StudentSubjectGradesDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.stream.Collectors;

public class XLSUtils {

    public static void prepareXLSDocument(List<StudentSubjectGradesDTO> records, Workbook workbook) {
        //TODO: add row numbers
        Sheet sheet = workbook.createSheet("Grades");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Ordinal Number");
        headerRow.createCell(1).setCellValue("Student Name");
        headerRow.createCell(2).setCellValue("Subject");
        headerRow.createCell(3).setCellValue("Exam");
        headerRow.createCell(4).setCellValue("Test");
        headerRow.createCell(5).setCellValue("Quiz");
        headerRow.createCell(6).setCellValue("Questioning");
        headerRow.createCell(7).setCellValue("Homework");
        headerRow.createCell(8).setCellValue("Other");
        headerRow.createCell(9).setCellValue("Average Grade");

        int currentRow = 1;
        for (StudentSubjectGradesDTO record : records) {
            Row row = sheet.createRow(currentRow);
            row.createCell(0).setCellValue(currentRow);
            row.createCell(1).setCellValue(record.getStudentName());
            row.createCell(2).setCellValue(record.getSubject());
            row.createCell(3).setCellValue(record.getExamGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(4).setCellValue(record.getTestGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(5).setCellValue(record.getQuizGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(6).setCellValue(record.getQuestioningGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(7).setCellValue(record.getHomeworkGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(8).setCellValue(record.getOtherGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")));
            row.createCell(9).setCellValue(Double.parseDouble(String.valueOf(record.getAverageGrade())));
            currentRow++;
        }
    }
}
