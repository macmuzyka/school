package com.school.controller;

import com.school.model.FileToImport;
import com.school.service.DatabaseBackupService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/database-backup")
public class DatabaseBackupController {
    private final DatabaseBackupService databaseBackupService;

    public DatabaseBackupController(final DatabaseBackupService databaseBackupService) {
        this.databaseBackupService = databaseBackupService;
    }

    @GetMapping
    public ResponseEntity<?> getDatabaseBackupFile() {
        try {
            FileToImport toImport = databaseBackupService.manuallyGetDatabaseDumpBackupFile();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toImport.getFileName() + "\"")
                    .body(toImport.getFileToImport());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restoreDatabaseFromFile(@RequestParam("backup") MultipartFile backup) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(databaseBackupService.restoreDatabaseFromFile(backup));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
