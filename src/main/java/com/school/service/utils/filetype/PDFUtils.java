package com.school.service.utils.filetype;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.school.model.dto.GradeDisplayDTO;
import com.school.model.dto.StudentSubjectGradesDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PDFUtils {
    private final static float fontSize = 10;
    private final static float[] columnWidths = {1, 1, 1, 1, 1, 1, 1, 1, 1, 2};

    public static Document prepareDocumentCells(List<StudentSubjectGradesDTO> records, String fullFilePAthWithoutExtension) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(fullFilePAthWithoutExtension));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Table headerTable = prepareHeaderTable();
        Table recordsTable = prepareRecordsTable();

        document.add(headerTable);
        document.add(populateRecordsTableWithData(records, recordsTable));
        return document;
    }

    private static Table prepareHeaderTable() throws IOException {
        Table headerTable = new Table(1);
        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Student grades Grouped by Subject [O.n] -> Ordinal number"));
        headerTable.addCell(styleLegenCell(headerCell, CellColoring.ORDINAL_NUMBER_COLUMN.rgb()));
        return headerTable;
    }

    private static Table prepareRecordsTable() throws IOException {
        Table recordsTable = new Table(columnWidths);

        Cell ordinalNumberCell = new Cell();
        styleLegenCell(ordinalNumberCell.add(new Paragraph("O.n.")), CellColoring.ORDINAL_NUMBER_COLUMN.rgb());
        recordsTable.addCell(ordinalNumberCell);


        Cell studentNameCell = new Cell();
        styleLegenCell(studentNameCell.add(new Paragraph("Student Name")), CellColoring.STUDENT_COLUMN.rgb());
        recordsTable.addCell(studentNameCell);

        Cell subjectNameCell = new Cell();
        styleLegenCell(subjectNameCell.add(new Paragraph("Subject")), CellColoring.SUBJECT_COLUMN.rgb());
        recordsTable.addCell(subjectNameCell);

        Cell examCellName = new Cell();
        styleLegenCell(examCellName.add(new Paragraph("Exam")), CellColoring.GRADES_COLUMN_SWITCH_0.rgb());
        recordsTable.addCell(examCellName);

        Cell testCellName = new Cell();
        styleLegenCell(testCellName.add(new Paragraph("Test")), CellColoring.GRADES_COLUMN_SWITCH_1.rgb());
        recordsTable.addCell(testCellName);

        Cell quizCellName = new Cell();
        styleLegenCell(quizCellName.add(new Paragraph("Quiz")), CellColoring.GRADES_COLUMN_SWITCH_0.rgb());
        recordsTable.addCell(quizCellName);

        Cell questioningCellName = new Cell();
        styleLegenCell(questioningCellName.add(new Paragraph("Questioning")), CellColoring.GRADES_COLUMN_SWITCH_1.rgb());
        recordsTable.addCell(questioningCellName);

        Cell homeworkCellName = new Cell();
        styleLegenCell(homeworkCellName.add(new Paragraph("Homework")), CellColoring.GRADES_COLUMN_SWITCH_0.rgb());
        recordsTable.addCell(homeworkCellName);

        Cell otherCellName = new Cell();
        styleLegenCell(otherCellName.add(new Paragraph("Other")), CellColoring.GRADES_COLUMN_SWITCH_1.rgb());
        recordsTable.addCell(otherCellName);

        Cell averageGradesCell = new Cell();
        styleLegenCell(averageGradesCell.add(new Paragraph("Average Grade")), CellColoring.AVERAGE_GRADE_COLUMN.rgb());
        recordsTable.addCell(averageGradesCell);
        return recordsTable;
    }

    private static Table populateRecordsTableWithData(List<StudentSubjectGradesDTO> records, Table recordsTable) throws IOException {
        int ordinalNumber = 1;
        for (StudentSubjectGradesDTO record : records) {
            Cell ordinalNumberCell = new Cell();
            ordinalNumberCell.add(new Paragraph(String.valueOf(ordinalNumber)));
            recordsTable.addCell(styleRecordCell(ordinalNumberCell, CellColoring.ORDINAL_NUMBER_COLUMN.rgb()));

            Cell studentCell = new Cell();
            studentCell.add(new Paragraph(record.getStudentName()));
            recordsTable.addCell(styleRecordCell(studentCell, CellColoring.STUDENT_COLUMN.rgb()));

            Cell subjectCell = new Cell();
            subjectCell.add(new Paragraph(record.getSubject()));
            recordsTable.addCell(styleRecordCell(subjectCell, CellColoring.SUBJECT_COLUMN.rgb()));

            Cell examGradesCell = new Cell();
            examGradesCell.add(new Paragraph(record.getExamGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","))));
            recordsTable.addCell(styleRecordCell(examGradesCell, CellColoring.GRADES_COLUMN_SWITCH_0.rgb()));

            Cell testGradesCell = new Cell();
            testGradesCell.add(new Paragraph(record.getTestGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","))));
            recordsTable.addCell(styleRecordCell(testGradesCell, CellColoring.GRADES_COLUMN_SWITCH_1.rgb()));

            Cell quizGradesCell = new Cell();
            quizGradesCell.add(new Paragraph(record.getQuizGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","))));
            recordsTable.addCell(styleRecordCell(quizGradesCell, CellColoring.GRADES_COLUMN_SWITCH_0.rgb()));


            Cell questioningGradesCell = new Cell();
            questioningGradesCell.add(new Paragraph(record.getQuestioningGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","))));
            recordsTable.addCell(styleRecordCell(questioningGradesCell, CellColoring.GRADES_COLUMN_SWITCH_1.rgb()));

            Cell homeworkGradesCell = new Cell();
            homeworkGradesCell.add(new Paragraph(record.getHomeworkGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","))));
            recordsTable.addCell(styleRecordCell(homeworkGradesCell, CellColoring.GRADES_COLUMN_SWITCH_0.rgb()));

            Cell otherGradesCell = new Cell();
            otherGradesCell.add(new Paragraph((record.getOtherGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(",")))));
            recordsTable.addCell(styleRecordCell(otherGradesCell, CellColoring.GRADES_COLUMN_SWITCH_1.rgb()));

            Cell averageGradeCell = new Cell();
            averageGradeCell.add(new Paragraph(String.valueOf(record.getAverageGrade())));
            recordsTable.addCell(styleRecordCell(averageGradeCell, CellColoring.AVERAGE_GRADE_COLUMN.rgb()));

            ordinalNumber++;
        }
        return recordsTable;
    }

    private static Cell styleLegenCell(Cell toStyle, DeviceRgb cellColor) throws IOException {
        return toStyle.setFont(PdfFontFactory.createFont())
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(cellColor);
    }

    private static Cell styleRecordCell(Cell toStyle, DeviceRgb cellColor) throws IOException {
        return toStyle.setFont(PdfFontFactory.createFont())
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.CENTER)
                .setMaxWidth(40)
                .setPadding(5)
                .setBackgroundColor(cellColor);
    }
}
