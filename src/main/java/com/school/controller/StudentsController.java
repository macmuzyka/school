package com.school.controller;

import com.schoolmodel.model.Grade;
import com.schoolmodel.model.GradeRaw;
import com.school.service.StudentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/school")
public class StudentsController {
    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @PostMapping("/add-students")
    public ResponseEntity<?> addStudents(@RequestParam("file") MultipartFile studentsFile) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentsService.addStudents(studentsFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-subject-grades")
    public ResponseEntity<?> addStudents(@RequestParam(value = "studentId", required = false) Long studentId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentsService.getSubjectGradesForStudent(studentId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all-students")
    public ResponseEntity<?> getStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentsService.getAllStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-grade")
    public ResponseEntity<?> addGrade(@RequestBody Grade grade) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentsService.addGrade(grade));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-raw-grade")
    public ResponseEntity<?> addRawGrade(@RequestBody GradeRaw grade) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentsService.addGrade(
                    grade.getGrade(),
                    grade.getSubject(),
                    grade.getStudentCode())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
