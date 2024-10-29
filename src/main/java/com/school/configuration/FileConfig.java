package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.file")
public class FileConfig {
    private final String name;
    private final String directory;

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
}
