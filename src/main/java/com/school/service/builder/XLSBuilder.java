package com.school.service.builder;

import com.school.configuration.FileConfig;
import com.school.model.*;
import com.schoolmodel.model.dto.SubjectGradesDTO;
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


public class XLSBuilder extends FileResource implements FileBuilder {
    private static final Logger log = LoggerFactory.getLogger(XLSBuilder.class);

    public XLSBuilder(String fileExtension, FileConfig fileConfig, String parametrizedFileNamePrefix) {
        super(fileExtension, fileConfig, parametrizedFileNamePrefix);
    }

    @Override
    public FileProviderResponse prepare(List<SubjectGradesDTO> records) {
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
