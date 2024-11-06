package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.file")
public class FileConfig {
    private final String name;
    private final String directory;
    private String optionalFileNamePrefix;

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


    public String getOptionalFileNamePrefix() {
        return optionalFileNamePrefix;
    }

    public void setOptionalFileNamePrefix(String optionalFileNamePrefix) {
        this.optionalFileNamePrefix = optionalFileNamePrefix;
    }
}
