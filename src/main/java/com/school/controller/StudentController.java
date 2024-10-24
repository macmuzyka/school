package com.school.controller;

import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.service.InputStudentsFromTextFileService;
import com.school.service.StudentService;
import com.schoolmodel.model.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add-student")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.addStudent(student));
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

    @GetMapping("/all-students")
    public ResponseEntity<?> getStudents(@RequestParam(value = "assigned", required = false) Boolean assignedParam) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents(assignedParam));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-student")
    public ResponseEntity<?> deleteStudent(@RequestParam(value = "studentId") long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.deleteStudent(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update-student")
    public ResponseEntity<?> editStudent(@RequestBody Student updatedStudent) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
