package com.school.service;

import com.school.model.FileToImport;
import com.school.model.exception.PgDumpExecutionException;
import com.school.model.response.BackupResponse;
import com.school.service.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TODO: testable manually getting database backup file & restoring database via this backup file using H2 database
@Service
public class DatabaseBackupService {
    private final PgDumpService pgDumpService;
    private final PgRestoreService pgRestoreService;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final Logger log = LoggerFactory.getLogger(DatabaseBackupService.class);

    public DatabaseBackupService(final PgDumpService pgDumpService, final PgRestoreService pgRestoreService, final SendNotificationToFrontendService sendNotificationToFrontendService) {
        this.pgDumpService = pgDumpService;
        this.pgRestoreService = pgRestoreService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
    }

    public FileToImport manuallyGetDatabaseDumpBackupFile() throws PgDumpExecutionException {
        return pgDumpService.execute(false);
    }

    public BackupResponse restoreDatabaseFromFile(MultipartFile file) {
        BackupResponse backupResponse = pgRestoreService.execute(file);
        sendNotificationToFrontendService.notifyFrontendAboutDatabaseRestoreResult(backupResponse);
        return backupResponse;
    }

    @Scheduled(cron = "0 0 0 * * SAT")
    public void weeklyStorageDatabaseBackup() {
        log.info("[SCHEDULED] === Weekly storage database backup === {}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(TimestampUtils.toSecondDisplayFormat)));
        FileToImport file = pgDumpService.execute(true);
        log.info("Saved file name: {}", file.getFileName());
    }
}
