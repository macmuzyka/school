package com.school.controller;

import com.school.model.nosql.mongodb.StudentDocument;
import com.school.repository.nosql.mongodb.StudentDocumentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongodb/students")
public class StudentDocumentController {
    private final StudentDocumentRepository studentDocumentRepository;

    public StudentDocumentController(StudentDocumentRepository studentDocumentRepository) {
        this.studentDocumentRepository = studentDocumentRepository;
    }

    @GetMapping
    public List<StudentDocument> getAllStudents() {
        return studentDocumentRepository.findAll();
    }

    @PostMapping
    public StudentDocument addStudent(@RequestBody StudentDocument student) {
        return studentDocumentRepository.save(student);
    }

    @GetMapping("/{id}")
    public StudentDocument getStudentById(@PathVariable String id) {
        return studentDocumentRepository.findById(id).orElse(null);
    }
}