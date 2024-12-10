package com.school.controller;

import com.school.model.OptionalRequestParams;
import com.school.model.dto.GradeDTO;
import com.school.service.EnvironmentService;
import com.school.service.GradeService;
import com.school.service.GradeProducingByKafkaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;
    private final EnvironmentService environmentService;
    private final GradeProducingByKafkaService gradeProducingByKafkaService;

    public GradeController(GradeService gradeService, EnvironmentService environmentService, GradeProducingByKafkaService gradeProducingByKafkaService) {
        this.gradeService = gradeService;
        this.environmentService = environmentService;
        this.gradeProducingByKafkaService = gradeProducingByKafkaService;
    }

    @PostMapping("/add-grade")
    public ResponseEntity<?> addRawGrade(@RequestBody GradeDTO grade) {
        try {
            Object response;
            if (environmentService.currentProfileOtherThanDevel()) {
                response = gradeProducingByKafkaService.produceGradeAndSendViaKafka(grade);
            } else {
                response = gradeService.addGrade(grade);
            }
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Response body was null");
            }
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
