package com.school;

import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.StudentRepository;
import com.school.model.entity.Student;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static com.school.utils.ValidationUtils.codeIsValidUUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("prod")
public class StudentTest {
    private final Logger log = LoggerFactory.getLogger(StudentTest.class);
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public SchoolClassRepository schoolClassRepository;
    @Autowired
    public SchoolRepository schoolRepository;
    @Value("#{'${app.config.available-subjects}'.split(',')}")
    public List<String> subjects;
    public Student savedStudent;

    @BeforeEach
    public void setUp() {
        savedStudent = studentRepository.save(new Student("John", "O'Connor", "123456789", UUID.randomUUID().toString(), LocalDate.now().minus(Period.ofYears(20)), false));
    }

    @Test
    public void createStudentTest() {
        assertEquals(1, studentRepository.findAll().size());
        assertEquals("John", savedStudent.getName());
        assertEquals("O'Connor", savedStudent.getSurname());
        assertTrue(codeIsValidUUID(savedStudent.getCode()));
        assertNull(savedStudent.getStudentGrades());
        assertFalse(savedStudent.isAssigned());
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
    }
}

