package com.school.controller;

import com.school.service.InputStudentsFromTextFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/input")
public class InputStudentsFromFileController {
    private final InputStudentsFromTextFileService inputStudentsFromTextFileService;
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromFileController.class);

    public InputStudentsFromFileController(InputStudentsFromTextFileService inputStudentsFromTextFileService) {
        this.inputStudentsFromTextFileService = inputStudentsFromTextFileService;
    }

    @PostMapping("/students-from-file")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addStudents(@RequestParam("file") MultipartFile studentsFile) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(inputStudentsFromTextFileService.addStudents(studentsFile, true));
        } catch (AccessDeniedException e) {
            log.error("AccessDeniedException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
