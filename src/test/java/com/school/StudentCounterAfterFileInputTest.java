package com.school;

import com.school.repository.StudentDuplicateErrorRepository;
import com.school.repository.StudentInsertErrorRepository;
import com.school.repository.StudentRepository;
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
public class StudentCounterAfterFileInputTest {
    private final Logger log = LoggerFactory.getLogger(StudentCounterAfterFileInputTest.class);
    @Autowired
    public InputStudentsFromTextFileService inputStudentsFromTextFileService;

    @Autowired
    public StudentRepository studentRepository;

    @Autowired
    public StudentInsertErrorRepository studentInsertErrorRepository;

    @Autowired
    public StudentDuplicateErrorRepository studentDuplicateErrorRepository;


    @BeforeEach
    public void setUp() throws IOException {
        log.info("Setting up for tests...");
        File studentsFile = new File("src/test/resources/students_list.txt");
        InputStream inputStream = new FileInputStream(studentsFile);
        MultipartFile mpf = new MockMultipartFile("file", studentsFile.getName(), "text/plain", inputStream);
        inputStudentsFromTextFileService.addStudents(mpf);
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
