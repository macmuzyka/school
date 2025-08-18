package com.school.controller;

import com.school.model.dto.ExistingStudentToClassDTO;
import com.school.service.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
public class ClassController {
    private final SchoolClassService schoolClassService;

    public ClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }


    @GetMapping("/all")
    public ResponseEntity<?> getClassesWithStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(schoolClassService.getClassesWithStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/student-to-class-action")
    public ResponseEntity<?> assignStudentToClass(@RequestBody ExistingStudentToClassDTO existingStudentToClassDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(schoolClassService.studentToClassAction(existingStudentToClassDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(schoolClassService.createNewClass());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
