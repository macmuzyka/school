package com.school.controller;

import com.schoolmodel.model.Grade;
import com.schoolmodel.model.GradeRaw;
import com.school.service.SchoolManagingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/school")
public class StudentsController {
    private final SchoolManagingService schoolManagingService;

    public StudentsController(SchoolManagingService schoolManagingService) {
        this.schoolManagingService = schoolManagingService;
    }

    @PostMapping("/add-students")
    public ResponseEntity<?> addStudents(@RequestParam("file") MultipartFile studentsFile) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(schoolManagingService.addStudents(studentsFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-subject-grades")
    public ResponseEntity<?> addStudents(@RequestParam(value = "studentId", required = false) Long studentId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(schoolManagingService.getSubjectGradesForStudent(studentId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all-students")
    public ResponseEntity<?> getStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(schoolManagingService.getAllStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all-classes")
    public ResponseEntity<?> getClassesWithStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(schoolManagingService.getClassesWithStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
