package com.school.service.builder;

import com.itextpdf.layout.Document;
import com.school.configuration.FileConfig;
import com.school.model.FileResource;
import com.school.model.FileBuilder;
import com.schoolmodel.model.dto.SubjectGradesDTO;
import com.schoolmodel.model.response.FileProviderResponse;
import com.schoolmodel.model.enums.FileStatus;
import com.school.service.utils.filetype.PDFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class PDFBuilder extends FileResource implements FileBuilder {
    private static final Logger log = LoggerFactory.getLogger(XLSBuilder.class);

    public PDFBuilder(String fileExtension, FileConfig fileConfig, String parametrizedFileNamePrefix) {
        super(fileExtension, fileConfig, parametrizedFileNamePrefix);
    }

    @Override
    public FileProviderResponse prepare(List<SubjectGradesDTO> records) {
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
