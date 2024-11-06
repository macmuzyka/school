package com.school.service.builder;

import com.school.configuration.FileConfig;
import com.school.model.*;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
import com.schoolmodel.model.response.FileProviderResponse;
import com.schoolmodel.model.enums.FileStatus;
import com.school.service.utils.filetype.XLSUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class XLSProvider extends FileResource implements FileProvider {
    private static final Logger log = LoggerFactory.getLogger(XLSProvider.class);

    public XLSProvider(String fileExtension, FileConfig fileConfig) {
        super(fileExtension, fileConfig);
    }

    @Override
    public FileProviderResponse build(List<StudentSubjectGradesDTO> records) {
        String resultMessage;
        try (Workbook workbook = new HSSFWorkbook();
             FileOutputStream outFile = new FileOutputStream(this.getFullPathWithoutExtension())) {

            XLSUtils.prepareXLSDocument(records, workbook);
            workbook.write(outFile);

            resultMessage = "XLS file created successfully.";
            log.info(resultMessage);
            return new FileProviderResponse(FileStatus.CREATED, records.size(), resultMessage);

        } catch (IOException e) {
            resultMessage = "XLS prepare -> Error while creating CSV file from student subject grades records!";
            log.error(resultMessage);
            log.error(e.getMessage());
            return new FileProviderResponse(FileStatus.ERROR_CREATING, 0, resultMessage);
        }
    }
}
