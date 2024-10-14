package com.school.model;

public abstract class BaseFile {
    private final String outputPath;
    private final String fileExtension;

    public BaseFile(String outputPath, String fileExtension) {
        this.outputPath = outputPath;
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
