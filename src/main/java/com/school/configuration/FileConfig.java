package com.school.configuration;

import com.school.model.enums.FileType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.file")
public class FileConfig {
    private final String name;
    private final String directory;
    private String optionalNamePrefix;
    private FileType fileType;

    public FileConfig(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }


    public String getOptionalNamePrefix() {
        return optionalNamePrefix;
    }

    public void setOptionalNamePrefix(String optionalNamePrefix) {
        this.optionalNamePrefix = optionalNamePrefix;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
