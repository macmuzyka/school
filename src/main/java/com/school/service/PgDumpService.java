package com.school.service;

import com.school.configuration.DatabaseBackupConfig;
import com.school.model.FileToImport;
import com.school.model.enums.PgTool;
import com.school.model.pgtool.PgDumpTool;
import com.school.model.pgtool.PgToolCredentials;
import com.school.model.exception.PgDumpExecutionException;
import com.school.service.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.school.service.utils.FileUtils.purifyDirectoryFromAllPreviousFiles;
import static com.school.service.utils.FileUtils.validateAndPrepareFile;

@Service
public class PgDumpService extends PgToolCredentials implements PgDumpTool {
    private final DatabaseBackupConfig databaseBackupConfig;
    private File backupDirectory;
    private String destinationBackupFile;
    private final Logger log = LoggerFactory.getLogger(PgDumpService.class);

    public PgDumpService(final DatabaseBackupConfig databaseBackupConfig) {
        this.databaseBackupConfig = databaseBackupConfig;
    }

    @Override
    public FileToImport execute(boolean storePermanently) {
        String pgDumpToolAbsolutePath = databaseBackupConfig.getBinDirectory() + File.separator + PgTool.pg_dump;
        log.debug("pg_dump tool absolute path: {}", pgDumpToolAbsolutePath);

        setDirectoryAndNameForCreatedFile(storePermanently);
        try {
            int exitCode = executeProcess(pgDumpToolAbsolutePath, destinationBackupFile);
            log.debug("pg_dump exit code: {}", exitCode);

            return validateAndPrepareFile(backupDirectory);
        } catch (Exception e) {
            throw new PgDumpExecutionException(e);
        }
    }

    private void setDirectoryAndNameForCreatedFile(boolean storePermanently) {
        if (storePermanently) {
            backupDirectory = new File(databaseBackupConfig.getScheduledBackupFolder());
            destinationBackupFile = buildFullFilePathWithDirectory(backupDirectory, TimestampUtils.toDayFileTimestamp());
        } else {
            backupDirectory = new File(databaseBackupConfig.getTemporaryBackupFolder());
            destinationBackupFile = buildFullFilePathWithDirectory(backupDirectory, TimestampUtils.toSecondFileTimestamp());
            purifyDirectoryFromAllPreviousFiles(backupDirectory);
        }
        log.debug("backup directory file: {}", destinationBackupFile);
        log.debug("backup destination file: {}", destinationBackupFile);
    }

    private String buildFullFilePathWithDirectory(File directory, String timeFormatter) {
        return directory.getPath() + File.separator +
                databaseBackupConfig.getBackupName() + timeFormatter + ".dump";
    }

    private int executeProcess(String pgDumpToolAbsolutePath, String destinationBackupFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                pgDumpToolAbsolutePath,
                "-U", "postgres",
                "-h", "localhost",
                "-F", "c",
                "-b",
                "-v",
                "-f", destinationBackupFile,
                databaseName
        );

        Map<String, String> environment = processBuilder.environment();
        environment.put("PGPASSWORD", databasePassword);

        log.debug("Executing command: {}", String.join(" ", processBuilder.command()));

        Process process = processBuilder.start();
        return process.waitFor();
    }
}