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
    private final StudentsFromListBuilderService studentsFromListBuilderService;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromTextFileService.class);

    public InputStudentsFromTextFileService(StudentRepository studentRepository,
                                            StudentsFromListBuilderService studentsFromListBuilderService,
                                            SendNotificationToFrontendService sendNotificationToFrontendService,
                                            EnvironmentService environmentService
    ) {
        this.studentRepository = studentRepository;
        this.studentsFromListBuilderService = studentsFromListBuilderService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
    }

    public List<StudentDTO> addStudents(MultipartFile studentsFile) {
        log.info("Adding students from file..");
        saveStudentsFromFile(studentsFile);
        return studentRepository.findAll().stream().map(StudentDTO::new).toList();
    }

    private void saveStudentsFromFile(MultipartFile studentsFile) {
        String result = studentsFromListBuilderService.saveStudentsFromFile(studentsFile);
        sendNotificationToFrontendService.notifyFrontendAboutUploadingFileStatus(result);
    }
}