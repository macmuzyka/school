package com.school.controller;

import com.school.service.GradeService;
import com.schoolmodel.model.Grade;
import com.schoolmodel.model.GradeRaw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/add-grade")
    public ResponseEntity<?> addGrade(@RequestBody Grade grade) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gradeService.addGrade(grade));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-raw-grade")
    public ResponseEntity<?> addRawGrade(@RequestBody GradeRaw grade) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gradeService.addGrade(
                    grade.getGrade(),
                    grade.getSubject(),
                    grade.getStudentCode())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
