package com.school.service;

import com.school.configuration.DatabaseBackupConfig;
import com.school.model.response.BackupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class PgRestoreService implements PgTool {
    private final DatabaseBackupConfig databaseBackupConfig;
    private final Logger log = LoggerFactory.getLogger(PgRestoreService.class);

    public PgRestoreService(final DatabaseBackupConfig databaseBackupConfig) {
        this.databaseBackupConfig = databaseBackupConfig;
    }

    @Override
    public BackupResponse execute(MultipartFile backupFile) {
        try {
            log.info("Attempting to restore database from file...");
            File temporaryFile = saveAndGetFile(backupFile);
            String backupAbsolutePath = temporaryFile.getAbsolutePath();
            String pgRestoreToolAbsolutePath = databaseBackupConfig.getBinDirectory() + File.separator + "pg_restore";
            String restoredDatabaseName = "schooll";

            int exitCode = executeProcess(pgRestoreToolAbsolutePath, restoredDatabaseName, backupAbsolutePath);
            log.debug("pg_restore exit code: {}", exitCode);
            return new BackupResponse(exitCode, prepareRestoreMessage(exitCode));
        } catch (Exception e) {
            return new BackupResponse(-1, e.getMessage());
        }
    }

    private File saveAndGetFile(MultipartFile backupFile) throws IOException {
        File savedFile = File.createTempFile("mpf_", "_" + backupFile.getOriginalFilename());
        backupFile.transferTo(savedFile);
        return savedFile;
    }

    private int executeProcess(String pgRestoreToolAbsolutePath, String restoredDatabaseName, String backupFileAbsolutePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                pgRestoreToolAbsolutePath,
                "-U", "postgres",
                "-h", "localhost",
                "-d", restoredDatabaseName,
                "--clean",
                "-v",
                backupFileAbsolutePath
        );

        Map<String, String> environment = processBuilder.environment();
        environment.put("PGPASSWORD", "postgres");

        log.debug("Executing command: {}", String.join(" ", processBuilder.command()));

//        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = processBuilder.start();
        return process.waitFor();
    }

    private String prepareRestoreMessage(int exitCode) {
        if (exitCode == 0) {
            return "Database backup restored successfully!";
        } else {
            return "Errors while restoring database backup.";
        }
    }
}
