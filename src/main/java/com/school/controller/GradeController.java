package com.school.controller;

import com.school.model.OptionalRequestParams;
import com.school.model.dto.GradeDTO;
import com.school.service.EnvironmentService;
import com.school.service.GradeService;
import com.school.service.GradeProducingByKafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;
    private final EnvironmentService environmentService;
    private final Optional<GradeProducingByKafkaService> gradeProducingByKafkaService;
    private final Logger log = LoggerFactory.getLogger(GradeController.class);

    public GradeController(GradeService gradeService, EnvironmentService environmentService, Optional<GradeProducingByKafkaService> gradeProducingByKafkaService) {
        this.gradeService = gradeService;
        this.environmentService = environmentService;
        this.gradeProducingByKafkaService = gradeProducingByKafkaService;
    }

    @PostMapping("/add-grade")
    public ResponseEntity<?> addRawGrade(@RequestBody GradeDTO grade) {
        try {
            if (environmentService.currentProfileOtherThanDevel()) {
                log.info("not devel");
                String status = gradeProducingByKafkaService.get().produceGradeAndSendViaKafka(grade);
                if (status.equalsIgnoreCase("OK")) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(status);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
                }
            } else {
                log.info("devel");
                return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.addGrade(grade));
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
