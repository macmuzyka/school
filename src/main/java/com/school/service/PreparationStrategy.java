package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.service.builder.CSVBuilder;
import com.school.service.builder.PDFBuilder;
import com.school.service.builder.XLSBuilder;
import org.springframework.stereotype.Service;

@Service
public class PreparationStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";
    private static final String pdfExtension = ".pdf";

    public static FileBuilder resolve(FileType type, FileConfig fileConfig) {
        return switch (type) {
            case CSV -> new CSVBuilder(csvExtension, fileConfig);
            case XLS -> new XLSBuilder(xlsExtension, fileConfig);
            case PDF -> new PDFBuilder(pdfExtension, fileConfig);
            default -> throw new IllegalArgumentException("File type " + type + " not supported yet!");
        };
    }
}
