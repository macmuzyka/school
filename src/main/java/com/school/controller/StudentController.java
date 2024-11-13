package com.school.controller;

import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.service.StudentService;
import com.schoolmodel.model.dto.StudentDTO;
import com.schoolmodel.model.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.addStudent(student));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getStudent(@RequestParam(value = "studentId") long studentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudent(studentId));
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

    @GetMapping("/all")
    public ResponseEntity<?> getStudents(@RequestParam(value = "assigned", required = false) Boolean assignedParam) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents(assignedParam));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudent(@RequestParam(value = "studentId") long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.deleteStudent(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> editStudent(@RequestBody StudentDTO updatedStudent) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/correct")
    public ResponseEntity<?> correctStudent(@RequestBody StudentDTO updatedStudent) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.correctStudent(updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/insert-error")
    public ResponseEntity<?> insertError() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getInputErrorStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
