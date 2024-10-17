package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ConfigurationProperties(prefix = "config.file")
public class FileConfig {
    private final String name;
    private final String directory;
    private final String fullPathWithoutExtension;
    private final String formatter;

    public FileConfig(String name, String directory, String formatter) {
        this.name = name;
        this.directory = directory;
        this.formatter = formatter;
        this.fullPathWithoutExtension = this.getDirectory() +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter)) +
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

    public String getFormatter() {
        return formatter;
    }
}
