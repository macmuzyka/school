package com.school.service;

import com.school.configuration.DatabaseBackupConfig;
import com.school.model.enums.PgTool;
import com.school.model.pgtool.PgRestoreTool;
import com.school.model.pgtool.PgToolCredentials;
import com.school.model.response.BackupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PgRestoreService extends PgToolCredentials implements PgRestoreTool {
    private final DatabaseBackupConfig databaseBackupConfig;
    private final Logger log = LoggerFactory.getLogger(PgRestoreService.class);

    public PgRestoreService(DatabaseBackupConfig databaseBackupConfig) {
        this.databaseBackupConfig = databaseBackupConfig;
    }

    @Override
    public BackupResponse execute(MultipartFile backupFile) {
        try {
            log.info("Attempting to restore database from file...");
            File temporaryFile = saveAndGetFileForPgRestore(backupFile);
            String backupAbsolutePath = temporaryFile.getAbsolutePath();
            String pgRestoreToolAbsolutePath = databaseBackupConfig.getBinDirectory() + File.separator + PgTool.pg_restore;

            BackupResponse backupResponse = executeProcess(pgRestoreToolAbsolutePath, backupAbsolutePath);
            log.debug("pg_restore exit code: {}", backupResponse.code());
            return backupResponse;
        } catch (Exception e) {
            return new BackupResponse(-1, e.getMessage());
        }
    }

    private File saveAndGetFileForPgRestore(MultipartFile backupFile) throws IOException {
        File savedFile = File.createTempFile("mpf_", "_" + backupFile.getOriginalFilename());
        backupFile.transferTo(savedFile);
        return savedFile;
    }

    private BackupResponse executeProcess(String pgRestoreToolAbsolutePath, String backupFileAbsolutePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                pgRestoreToolAbsolutePath,
                "-U", "postgres",
                "-h", "localhost",
                "-d", databaseName,
                "--clean",
                "-v",
                backupFileAbsolutePath
        );

        Map<String, String> environment = processBuilder.environment();
        environment.put("PGPASSWORD", databasePassword);

        log.debug("Executing command: {}", String.join(" ", processBuilder.command()));

        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        Process process = processBuilder.start();
        String pgRestoreErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                .lines()
                .collect(Collectors.joining("\n"));

        return new BackupResponse(process.waitFor(), pgRestoreErrors);
    }
}
