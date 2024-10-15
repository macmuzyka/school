package com.school.service.utils.filetype;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.school.configuration.FileConfig;
import com.school.model.BaseFile;
import com.school.model.FileBuilder;
import com.school.model.dto.SubjectGradesDTO;
import com.school.model.response.FileProviderResponse;
import com.school.model.response.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileOutputStream;
import java.util.List;

public class PDFUtils extends BaseFile implements FileBuilder {
    private static final Logger log = LoggerFactory.getLogger(XLSUtils.class);

    public PDFUtils(String fileExtension, FileConfig fileConfig) {
        super(fileExtension, fileConfig);
    }

    @Override
    public FileProviderResponse prepare(List<SubjectGradesDTO> records) {
        String resultMessage;
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(this.getFullPathWithoutExtension()));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Table table = new Table(4);
            table.addCell("Student Name");
            table.addCell("Subject");
            table.addCell("Grades");
            table.addCell("Average Grades");

            for (SubjectGradesDTO record : records) {
                table.addCell(record.getStudentName());
                table.addCell(record.getSubject());
                table.addCell(record.getGrades());
                table.addCell(String.valueOf(record.getAverageGrade()));
            }

            document.add(table);
            document.close();
            resultMessage = "PDF file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, records.size(), resultMessage);

        } catch (Exception e) {
            resultMessage = "PDF prepare -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
