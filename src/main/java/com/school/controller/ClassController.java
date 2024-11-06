package com.school.controller;

import com.school.service.ClassService;
import com.schoolmodel.model.dto.ExistingStudentToClassDTO;
import com.schoolmodel.model.dto.NewClassDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }


    @GetMapping("/all-classes")
    public ResponseEntity<?> getClassesWithStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(classService.getClassesWithStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/student-to-class-action")
    public ResponseEntity<?> assignStudentToClass(@RequestBody ExistingStudentToClassDTO existingStudentToClassDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(classService.studentToClassAction(existingStudentToClassDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-class")
    public ResponseEntity<?> createClass(@RequestBody NewClassDTO newClassDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(classService.createClass(newClassDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
