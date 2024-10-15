package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.service.utils.filetype.CSVUtils;
import com.school.service.utils.filetype.PDFUtils;
import com.school.service.utils.filetype.XLSUtils;
import org.springframework.stereotype.Service;

@Service
public class PreparationStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";
    private static final String pdfExtenstion = ".pdf";

    public static FileBuilder resolve(FileType type, FileConfig fileConfig) {
        return switch (type) {
            case CSV -> {
                yield new CSVUtils(csvExtension, fileConfig);
            }
            case XLS -> {
                yield new XLSUtils(xlsExtension, fileConfig);
            }
            case PDF -> {
                yield new PDFUtils(pdfExtenstion, fileConfig);
            }
            //TODO: pdf
            default -> {
                throw new IllegalArgumentException("File type " + type + " not supported yet!");
            }
        };
    }
}
