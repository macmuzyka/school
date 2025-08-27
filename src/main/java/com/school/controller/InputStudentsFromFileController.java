package com.school.controller;

import com.school.service.InputStudentsFromTextFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/input")
public class InputStudentsFromFileController {
    private final InputStudentsFromTextFileService inputStudentsFromTextFileService;

    public InputStudentsFromFileController(InputStudentsFromTextFileService inputStudentsFromTextFileService) {
        this.inputStudentsFromTextFileService = inputStudentsFromTextFileService;
    }

    @PostMapping("/students-from-file")
    public ResponseEntity<?> addStudents(@RequestParam("file") MultipartFile studentsFile) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(inputStudentsFromTextFileService.addStudents(studentsFile, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
