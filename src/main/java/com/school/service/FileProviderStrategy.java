package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileProvider;
import com.schoolmodel.model.enums.FileType;
import com.school.service.builder.CSVProvider;
import com.school.service.builder.PDFProvider;
import com.school.service.builder.XLSProvider;
import org.springframework.stereotype.Service;

@Service
public class FileProviderStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";
    private static final String pdfExtension = ".pdf";

    public static FileProvider resolve(FileType type, FileConfig fileConfig) {
        return switch (type) {
            case CSV -> new CSVProvider(csvExtension, fileConfig);
            case XLS -> new XLSProvider(xlsExtension, fileConfig);
            case PDF -> new PDFProvider(pdfExtension, fileConfig);
            default -> throw new IllegalArgumentException("File type " + type + " not supported yet!");
        };
    }
}
