package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.schoolmodel.model.enums.FileType;
import com.school.service.builder.CSVBuilder;
import com.school.service.builder.PDFBuilder;
import com.school.service.builder.XLSBuilder;
import org.springframework.stereotype.Service;

@Service
public class PreparationStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";
    private static final String pdfExtension = ".pdf";

    public static FileBuilder resolve(FileType type, FileConfig fileConfig, String parametrizedFileNamePrefix) {
        return switch (type) {
            case CSV -> new CSVBuilder(csvExtension, fileConfig, parametrizedFileNamePrefix);
            case XLS -> new XLSBuilder(xlsExtension, fileConfig, parametrizedFileNamePrefix);
            case PDF -> new PDFBuilder(pdfExtension, fileConfig, parametrizedFileNamePrefix);
            default -> throw new IllegalArgumentException("File type " + type + " not supported yet!");
        };
    }
}
