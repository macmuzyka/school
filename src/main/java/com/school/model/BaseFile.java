package com.school.model;

import com.school.configuration.FileConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseFile {
    private final String fileName;
    private final String fileDirectory;
    private final String fullPathWithoutExtenstion;
    private final String fileExtension;
    private final FileConfig fileConfig;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public BaseFile(String fileExtension, FileConfig fileConfig) {
        this.fileConfig = fileConfig;
        this.fileName = fileConfig.getName();
        this.fileDirectory = fileConfig.getDirectory();
        this.fileExtension = fileExtension;
        this.fullPathWithoutExtenstion = this.getFileDirectory() +
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

    public String getFullPathWithoutExtenstion() {
        return fullPathWithoutExtenstion;
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }
}
