package com.school;

import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.StudentRepository;
import com.school.model.entity.School;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.Student;
import com.school.model.entity.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.school.utils.ValidationUtils.codeIsValidUUID;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("devel")
public class StudentTest {
    private final Logger log = LoggerFactory.getLogger(StudentTest.class);
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public SchoolClassRepository schoolClassRepository;
    @Autowired
    public SchoolRepository schoolRepository;
    @Value("#{'${available.subjects}'.split(',')}")
    public List<String> subjects;
    public Student savedStudent;
    public String classOne = "Class 1";
    public String classTwo = "Class 2";
    public String classThree = "Class 3";

    @BeforeEach
    public void setUp() {
        SchoolClass class1 = schoolClassRepository.save(new SchoolClass(classOne, subjects.stream().map(Subject::new).toList()));
        SchoolClass class2 = schoolClassRepository.save(new SchoolClass(classTwo, subjects.stream().map(Subject::new).toList()));
        SchoolClass class3 = schoolClassRepository.save(new SchoolClass(classThree, subjects.stream().map(Subject::new).toList()));

        ArrayList<SchoolClass> classes = new ArrayList<>();
        classes.add(class1);
        classes.add(class2);
        classes.add(class3);
        schoolRepository.save(new School("SCHOOL ONE", classes));

        savedStudent = studentRepository.save(new Student("John", "O'Connor", "123456789", UUID.randomUUID().toString(), LocalDate.now().minus(Period.ofYears(20)),  false));
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
}

