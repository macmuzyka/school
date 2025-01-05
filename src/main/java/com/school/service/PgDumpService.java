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
public class PgDumpService implements PgTool {
    private final DatabaseBackupConfig databaseBackupConfig;
    private final Logger log = LoggerFactory.getLogger(PgDumpService.class);

    public PgDumpService(final DatabaseBackupConfig databaseBackupConfig) {
        this.databaseBackupConfig = databaseBackupConfig;
    }

    @Override
    public BackupResponse execute(MultipartFile file) {
        try {
            String pgDumpToolAbsolutePath = databaseBackupConfig.getBinDirectory() + File.separator + "pg_dump";
            String destinationBackupFile = databaseBackupConfig.getBackupFolder() + File.separator + databaseBackupConfig.getBackupName() + ".dump";

            int exitCode = executeProcess(pgDumpToolAbsolutePath, destinationBackupFile);
            log.debug("pg_dump exit code: {}", exitCode);

            return new BackupResponse(exitCode, "Database created successfully" + additionalInfo(exitCode));
        } catch (Exception e) {
            return new BackupResponse(-1, e.getMessage());
        }
    }

    private int executeProcess(final String pgDumpToolAbsolutePath, final String destinationBackupFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                pgDumpToolAbsolutePath,
                "-U", "postgres",
                "-h", "localhost",
                "-F", "c",
                "-b",
                "-v",
                "-f", destinationBackupFile,
                "school"
        );

        Map<String, String> environment = processBuilder.environment();
        environment.put("PGPASSWORD", "postgres");

        log.debug("Executing command: {}", String.join(" ", processBuilder.command()));

        Process process = processBuilder.start();
        return process.waitFor();
    }

    private String additionalInfo(int exitCode) {
        if (exitCode == 1) {
            return " with minor warnings.";
        } else {
            return "!";
        }
    }
}
