package com.school.controller;

import com.school.service.FileProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileProviderService fileProviderService;

    public FileController(FileProviderService fileProviderService) {
        this.fileProviderService = fileProviderService;
    }

    @GetMapping("/produce")
    public ResponseEntity<?> getCsvFile(@RequestParam String fileType,
                                        @RequestParam(value = "studentId", required = false, defaultValue = "0") String studentId,
                                        @RequestParam(value = "subjectName", required = false) String subjectName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fileProviderService.produceFile(fileType, studentId, subjectName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
