package com.school.controller;

import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.service.SchoolManagingService;
import com.school.service.StudentService;
import com.schoolmodel.model.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final SchoolManagingService schoolManagingService;
    private final StudentService studentService;

    public StudentController(SchoolManagingService schoolManagingService, StudentService studentService) {
        this.schoolManagingService = schoolManagingService;
        this.studentService = studentService;
    }

    @PostMapping("/add-students")
    public ResponseEntity<?> addStudents(@RequestParam("file") MultipartFile studentsFile) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(schoolManagingService.addStudents(studentsFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all-students")
    public ResponseEntity<?> getStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-student-to-class")
    public ResponseEntity<?> addStudentToClass(@RequestBody NewStudentWithClassDTO studentDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.addStudentAndAssignToClass(studentDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-student")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.addStudent(student));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
