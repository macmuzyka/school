package com.school.service.builder;

import com.itextpdf.layout.Document;
import com.school.configuration.FileConfig;
import com.school.model.FileResource;
import com.school.model.FileProvider;
import com.school.model.dto.StudentSubjectGradesDTO;
import com.school.model.response.FileProviderResponse;
import com.school.model.enums.FileStatus;
import com.school.service.utils.filetype.PDFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class PDFProvider extends FileResource implements FileProvider {
    private static final Logger log = LoggerFactory.getLogger(XLSProvider.class);

    public PDFProvider(String fileExtension, FileConfig fileConfig) {
        super(fileExtension, fileConfig);
    }

    @Override
    public FileProviderResponse build(List<StudentSubjectGradesDTO> records) {
        String resultMessage;
        try {
            Document document = PDFUtils.prepareDocumentCells(records, this.getFullPathWithoutExtension());
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
