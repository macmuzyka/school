package com.school.integration;

import com.school.repository.sql.SchoolClassRepository;
import com.school.repository.sql.StudentRepository;
import com.school.service.SchoolClassService;
import com.school.service.StudentService;
import com.school.model.dto.ExistingStudentToClassDTO;
import com.school.model.dto.ClassDTO;
import com.school.model.dto.StudentDTO;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.Student;
import com.school.model.enums.ClassAction;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("prod")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassTests {
    @Autowired
    public StudentService studentService;
    @Autowired
    public SchoolClassService schoolClassService;
    @Autowired
    public SchoolClassRepository schoolClassRepository;
    @Autowired
    public StudentRepository studentRepository;
    private Student savedStudent;
    private Student additionalStudent;
    private final long primaryClassId = 1L;
    private final long movedToClassId = 2L;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
        savedStudent = studentService.addStudent(new StudentDTO(new Student(
                        "Ad",
                        "O'Ding",
                        "10101022222",
                        LocalDate.now().minus(Period.ofYears(20)),
                        false)
                )
        );
        additionalStudent = studentService.addStudent(new StudentDTO(new Student(
                        "Rem",
                        "O'Ving",
                        "01010133333",
                        LocalDate.now().minus(Period.ofYears(25)),
                        false)
                )
        );
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    @Transactional
    @Order(1)
    public void assignStudentToClassTest() {
        ClassDTO studentAddedClass = addStudentToClass(savedStudent);
        assertEquals(1, studentAddedClass.getClassStudents().size());
    }


    @Test
    @Transactional
    @Order(2)
    public void moveStudentToOtherClassTest() {
        SchoolClass newClass = schoolClassService.createNewClass();

        int preMoveSize = newClass.getClassStudents().size();
        moveStudentToOtherClass(additionalStudent, newClass.getId());
        SchoolClass movedStudentClassAfterUpdate = fetchUpdatedObject(newClass.getId());
        int postMoveSize = movedStudentClassAfterUpdate.getClassStudents().size();

        assertTrue(preMoveSize < postMoveSize);
    }

    private SchoolClass fetchUpdatedObject(long id) {
        Optional<SchoolClass> optionalSchoolClass = schoolClassRepository.findById(id);
        if (optionalSchoolClass.isPresent()) {
            return optionalSchoolClass.get();
        } else {
            throw new IllegalArgumentException("Error fetching updated object with id: " + id);
        }
    }

    @Test
    @Transactional
    @Order(3)
    public void removeStudentFromClassTest() {
        Student student = additionalStudent;
        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(primaryClassId);
        if (schoolClassOptional.isPresent()) {
            int pre = addStudentToClass(student).getClassStudents().size();
            int post = removeStudentFromClass(student).getClassStudents().size();
            assertTrue(pre > post);
        } else {
            throw new IllegalArgumentException("School class was not properly added during database warmup!");
        }
    }

    private ClassDTO addStudentToClass(Student student) {
        return schoolClassService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), primaryClassId, ClassAction.ADD));
    }

    private void moveStudentToOtherClass(Student student, Long toClassId) {
        schoolClassService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), toClassId, ClassAction.MOVE));
    }

    private ClassDTO removeStudentFromClass(Student student) {
        return schoolClassService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), primaryClassId, ClassAction.REMOVE));
    }
}
