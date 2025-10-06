package com.school.service;

import com.school.model.dto.StudentDTO;
import com.school.repository.sql.StudentRepository;
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
                                            SendNotificationToFrontendService sendNotificationToFrontendService
    ) {
        this.studentRepository = studentRepository;
        this.studentsFromListBuilderService = studentsFromListBuilderService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
    }

    public List<StudentDTO> addStudents(MultipartFile studentsFile, boolean assign) {
        log.info("Adding students from file..");
        saveStudentsFromFile(studentsFile, assign);
        return studentRepository.findAll().stream().map(StudentDTO::new).toList();
    }

    private void saveStudentsFromFile(MultipartFile studentsFile, boolean assign) {
        String result = studentsFromListBuilderService.saveStudentsFromFile(studentsFile, assign);
        sendNotificationToFrontendService.notifyFrontendAboutUploadingFileStatus(result);
    }
}