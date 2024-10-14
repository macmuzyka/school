package com.school.service.utils.filetype;

import com.school.model.FileBuilder;
import com.school.model.request.FileType;
import com.school.service.utils.filetype.csv.CsvUtils;
import com.school.service.utils.filetype.xls.XlsUtils;

public class PreparationStrategy {
    private static final String csvExtension = ".csv";
    private static final String xlsExtension = ".xls";

    public static FileBuilder resolve(String filesDestinationPath, FileType type) throws IllegalAccessException {
        switch (type) {
            case CSV, csv -> {
                return new CsvUtils(filesDestinationPath, csvExtension);
            }
            case XLS, xls -> {
                return new XlsUtils(filesDestinationPath, xlsExtension);
            }
            default -> throw new IllegalAccessException("FileType: " + type + " not supported yet!");
        }
    }
}
