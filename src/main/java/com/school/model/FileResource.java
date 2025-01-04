package com.school.model;

import com.school.configuration.FileConfig;
import com.school.service.utils.TimestampUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class FileResource {
    private final String fileName;
    private final String fileDirectory;
    private final String fullPathWithoutExtension;
    private final String fileExtension;
    private final FileConfig fileConfig;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimestampUtils.toSecondFileFormat);

    public FileResource(String fileExtension, FileConfig fileConfig) {
        this.fileConfig = fileConfig;
        this.fileName = fileConfig.getName();
        this.fileDirectory = fileConfig.getDirectory();
        this.fileExtension = fileExtension;
        this.fullPathWithoutExtension = this.getFileDirectory() + File.separator +
                fileConfig.getStudentsGradesSubdirectory() + File.separator +
                fileConfig.getOptionalNamePrefix() +
                LocalDateTime.now().format(this.getFormatter()) +
                "_" +
                this.getFileName() +
                this.getFileExtension();
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public String getFullPathWithoutExtension() {
        return fullPathWithoutExtension;
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }
}
