package com.school.controller;

import com.school.model.OptionalRequestParams;
import com.school.service.GradeService;
import com.schoolmodel.model.dto.GradeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/add-grade")
    public ResponseEntity<?> addRawGrade(@RequestBody GradeDTO grade) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.addGrade(grade.getValue(), grade.getSubjectId(), grade.getStudentId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-subject-grades")
    public ResponseEntity<?> addStudents(@RequestBody OptionalRequestParams params) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.getSubjectGradesForStudents(params));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
