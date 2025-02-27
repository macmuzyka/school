package com.school.controller;

import com.school.model.FileToImport;
import com.school.model.dto.NewStudentWithClassDTO;
import com.school.model.dto.StudentDTO;
import com.school.service.SendNotificationToFrontendService;
import com.school.service.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;

    public StudentController(StudentService studentService, final SendNotificationToFrontendService sendNotificationToFrontendService) {
        this.studentService = studentService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody StudentDTO student) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(student));
        } catch (IllegalArgumentException iae) {
            sendNotificationToFrontendService.notifyFrontendAboutAddedStudent(iae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(iae.getMessage());
        } catch (Exception e) {
            sendNotificationToFrontendService.notifyFrontendAboutAddedStudent(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getStudent(@RequestParam(value = "studentId") Long studentId) {
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
    public ResponseEntity<?> deleteStudent(@RequestParam(value = "studentId") Long id) {
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
        } catch (IllegalArgumentException iae) {
            sendNotificationToFrontendService.notifyFrontendAboutStudentUpdateStatus(iae.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            sendNotificationToFrontendService.notifyFrontendAboutStudentUpdateStatus(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/import-students-list-file")
    public ResponseEntity<?> importStudentsListFile() {
        try {
            FileToImport toImport = studentService.importFile();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toImport.getFileName() + "\"")
                    .body(toImport.getFileToImport());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
