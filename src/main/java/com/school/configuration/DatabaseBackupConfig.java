package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pg.backup.config")
public class DatabaseBackupConfig {
    private final String binDirectory;
    private final String backupName;
    private final String backupFolder;

    public DatabaseBackupConfig(final String binDirectory, final String backupName, final String backupFolder) {
        this.binDirectory = binDirectory;
        this.backupName = backupName;
        this.backupFolder = backupFolder;
    }

    public String getBinDirectory() {
        return binDirectory;
    }

    public String getBackupName() {
        return backupName;
    }

    public String getBackupFolder() {
        return backupFolder;
    }
}
