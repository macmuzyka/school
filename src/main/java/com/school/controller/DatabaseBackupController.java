package com.school.controller;

import com.school.service.DatabaseBackupService;
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
    public ResponseEntity<?> saveDatabaseBackup() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(databaseBackupService.saveDatabaseBackup());
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
