package com.school.service.utils.filetype;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.school.model.dto.StudentSubjectGradesDTO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PDFUtils {
    public static Document prepareDocumentCells(List<StudentSubjectGradesDTO> records, String fullFilePAthWithoutExtension) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(fullFilePAthWithoutExtension));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Table headerTable = prepareHeaderTable();
        Table recordsTable = prepareRecordsTable();

        document.add(headerTable);
        document.add(populateRecordsTableWithData(records, recordsTable));
        return document;
    }

    private static Table prepareHeaderTable() {
        Table headerTable = new Table(1);
        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Student grades Grouped by Subject [O.n] -> Ordinal number"));
        headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        headerCell.setFontColor(ColorConstants.BLACK);
        headerTable.addCell(headerCell);
        return headerTable;
    }

    private static Table prepareRecordsTable() {
        //TODO: add row numbers
        Table recordsTable = new Table(5);
        Cell ordinalNumberCell = new Cell();
        ordinalNumberCell.add(new Paragraph("O.n."));

        Cell studentNameCell = new Cell();
        studentNameCell.add(new Paragraph("Student Name"));
        studentNameCell.setBackgroundColor(new DeviceRgb(211, 211, 211));
        recordsTable.addCell(ordinalNumberCell);
        recordsTable.addCell(studentNameCell);
        recordsTable.addCell("Subject");
        recordsTable.addCell("Grades");
        Cell averageGradesCell = new Cell();
        averageGradesCell.add(new Paragraph("Average Grades")).setBackgroundColor(new DeviceRgb(211, 211, 211));
        recordsTable.addCell(averageGradesCell);
        return recordsTable;
    }

    private static Table populateRecordsTableWithData(List<StudentSubjectGradesDTO> records, Table recordsTable) {
        int ordinalNumber = 1;
        for (StudentSubjectGradesDTO record : records) {
            Cell ordinalNumberCell = new Cell();
            ordinalNumberCell.add(new Paragraph(String.valueOf(ordinalNumber)));
            recordsTable.addCell(ordinalNumberCell);

            Cell studentCell = new Cell();
            studentCell.add(new Paragraph(record.getStudentName())).setBackgroundColor(new DeviceRgb(220, 220, 220));
            recordsTable.addCell(studentCell);

            recordsTable.addCell(record.getSubject());
            recordsTable.addCell(record.getGrades());

            Cell averageGradeCell = new Cell();
            averageGradeCell.add(new Paragraph(String.valueOf(record.getAverageGrade()))).setBackgroundColor(new DeviceRgb(220, 220, 220));
            recordsTable.addCell(averageGradeCell);

            ordinalNumber++;
        }
        return recordsTable;
    }
}
