package com.school.controller;

import com.school.service.FileProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileProviderService fileProviderService;

    public FileController(FileProviderService fileProviderService) {
        this.fileProviderService = fileProviderService;
    }

    @GetMapping("/produce-csv")
    public ResponseEntity<?> getCsvFile() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fileProviderService.getCsvFile());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/produce-xls")
    public ResponseEntity<?> getXlsFile() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fileProviderService.getXlsFile());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
