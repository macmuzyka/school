package com.school.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ConfigurationProperties
public class FileConfig {
    @Value("${config.file.name}")
    private final String name;
    @Value("${config.file.directory}")
    private final String directory;
    private final String fullPathWithoutExtension;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public FileConfig(String name, String directory) {
        this.name = name;
        this.directory = directory;
        this.fullPathWithoutExtension = this.getDirectory() +
                LocalDateTime.now().format(this.getFormatter()) +
                "_" +
                this.getName();
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public String getFullPathWithoutExtension() {
        return fullPathWithoutExtension;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
