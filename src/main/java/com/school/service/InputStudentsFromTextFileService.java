package com.school.service;

import com.school.repository.*;
import com.school.model.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class InputStudentsFromTextFileService {
    private final StudentRepository studentRepository;
    private final SeedMockGradesService seedMockGradesService;
    private final StudentsFromListBuilderService studentsFromListBuilderService;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final EnvironmentService environmentService;
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromTextFileService.class);

    public InputStudentsFromTextFileService(StudentRepository studentRepository,
                                            SeedMockGradesService seedMockGradesService,
                                            StudentsFromListBuilderService studentsFromListBuilderService,
                                            SendNotificationToFrontendService sendNotificationToFrontendService,
                                            EnvironmentService environmentService
    ) {
        this.studentRepository = studentRepository;
        this.seedMockGradesService = seedMockGradesService;
        this.studentsFromListBuilderService = studentsFromListBuilderService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
        this.environmentService = environmentService;
    }

    public List<StudentDTO> addStudents(MultipartFile studentsFile) {
        log.info("Adding students from file..");
        saveStudentsFromFile(studentsFile);
        populateStudentsWithGradesIfDevelProfileIsActive();
        return studentRepository.findAll().stream().map(StudentDTO::new).toList();
    }

    private void saveStudentsFromFile(MultipartFile studentsFile) {
        String result = studentsFromListBuilderService.saveStudentsFromFile(studentsFile);
        sendNotificationToFrontendService.notifyFrontendAboutUploadingFileStatus(result);
    }

    private void populateStudentsWithGradesIfDevelProfileIsActive() {
        if (databaseShouldBeSeeded()) {
            log.info("Condition to seed randomized grades among student met, proceeding to populate");
            seedMockGradesService.seedStudentsWithRandomizedGrades();
        } else {
            log.debug("Not populating added students with randomized grades");
        }
    }

    private boolean databaseShouldBeSeeded() {
        return environmentService.profileOtherThanDefaultIsActive() && seedMockGradesService.notPopulatedYet();
    }
}