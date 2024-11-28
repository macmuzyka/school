package com.school.controller;

import com.school.model.FileToImport;
import com.school.model.OptionalRequestParams;
import com.school.service.FileProviderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileProviderService fileProviderService;

    public FileController(FileProviderService fileProviderService) {
        this.fileProviderService = fileProviderService;
    }

    @PostMapping("/produce")
    public ResponseEntity<?> getFileWithGrades(@RequestBody OptionalRequestParams params) {
        try {
            FileToImport toImport = fileProviderService.produceFile(params);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toImport.getFileName() + "\"")
                    .body(toImport.getFileToImport());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
