package com.school.service;

import com.school.model.response.BackupResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DatabaseBackupService {
    private final PgDumpService pgDumpService;
    private final PgRestoreService pgRestoreService;

    public DatabaseBackupService(final PgDumpService pgDumpService, final PgRestoreService pgRestoreService) {
        this.pgDumpService = pgDumpService;
        this.pgRestoreService = pgRestoreService;
    }

    public BackupResponse saveDatabaseBackup() {
        return pgDumpService.execute(null);
    }

    public BackupResponse restoreDatabaseFromFile(MultipartFile file) {
        return pgRestoreService.execute(file);
    }
}
