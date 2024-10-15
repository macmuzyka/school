package com.school.service.utils.filetype;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.service.utils.filetype.csv.CsvUtils;
import com.school.service.utils.filetype.xls.XlsUtils;
import org.springframework.stereotype.Service;

@Service
public class PreparationStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";

    public static FileBuilder resolve(FileType type, FileConfig fileConfig) throws IllegalAccessException {
        return switch (type) {
            case CSV -> {
                yield new CsvUtils(csvExtension, fileConfig);
            }
            case XLS -> {
                yield new XlsUtils(xlsExtension, fileConfig);
            }
            //TODO: pdf
            default -> {
                throw new IllegalArgumentException("File type " + type + " not supported yet!");
            }
        };
    }
}
