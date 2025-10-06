package com.school.integration;

import com.school.repository.sql.StudentDuplicateErrorRepository;
import com.school.repository.sql.StudentInsertErrorRepository;
import com.school.repository.sql.StudentRepository;
import com.school.service.InputStudentsFromTextFileService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("prod")
public class StudentCounterAfterFileInputTests {
    private final Logger log = LoggerFactory.getLogger(StudentCounterAfterFileInputTests.class);
    public final InputStudentsFromTextFileService inputStudentsFromTextFileService;
    public final StudentRepository studentRepository;
    public final StudentInsertErrorRepository studentInsertErrorRepository;
    public final StudentDuplicateErrorRepository studentDuplicateErrorRepository;

    @Autowired
    public StudentCounterAfterFileInputTests(InputStudentsFromTextFileService inputStudentsFromTextFileService, StudentRepository studentRepository, StudentInsertErrorRepository studentInsertErrorRepository, StudentDuplicateErrorRepository studentDuplicateErrorRepository) {
        this.inputStudentsFromTextFileService = inputStudentsFromTextFileService;
        this.studentRepository = studentRepository;
        this.studentInsertErrorRepository = studentInsertErrorRepository;
        this.studentDuplicateErrorRepository = studentDuplicateErrorRepository;
    }


    @BeforeEach
    public void setUp() throws IOException {
        log.info("Setting up for tests...");
        studentRepository.deleteAll();
        File studentsFile = new File("src/test/resources/students_list.txt");
        InputStream inputStream = new FileInputStream(studentsFile);
        MultipartFile mpf = new MockMultipartFile("file", studentsFile.getName(), "text/plain", inputStream);
        inputStudentsFromTextFileService.addStudents(mpf, true);
    }

    @Test
    @Transactional
    public void thereShouldBe97ProperlyInsertedStudents() {
        assertEquals(97, studentRepository.findAll().size());
    }

    @Test
    @Transactional
    public void thereShouldBe2StudentDuplicates() {
        assertEquals(2, studentDuplicateErrorRepository.findAll().size());
    }

    @Test
    @Transactional
    public void thereShouldBe1StudentInsertError() {
        assertEquals(1, studentInsertErrorRepository.findAll().size());
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
    }
}
