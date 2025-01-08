package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pg.backup.config")
public class DatabaseBackupConfig {
    private final String binDirectory;
    private final String backupName;
    private final String temporaryBackupFolder;
    private final String scheduledBackupFolder;


    public DatabaseBackupConfig(final String binDirectory, final String backupName, final String temporaryBackupFolder, final String scheduledBackupFolder) {
        this.binDirectory = binDirectory;
        this.backupName = backupName;
        this.temporaryBackupFolder = temporaryBackupFolder;
        this.scheduledBackupFolder = scheduledBackupFolder;
    }

    public String getBinDirectory() {
        return binDirectory;
    }

    public String getBackupName() {
        return backupName;
    }

    public String getTemporaryBackupFolder() {
        return temporaryBackupFolder;
    }

    public String getScheduledBackupFolder() {
        return scheduledBackupFolder;
    }
}
