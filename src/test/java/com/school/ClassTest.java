package com.school;

import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.service.ClassService;
import com.school.service.StudentService;
import com.schoolmodel.model.dto.ExistingStudentToClassDTO;
import com.schoolmodel.model.dto.SimpleClassDTO;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.enums.ClassAction;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("devel")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassTest {
    private final Logger log = LoggerFactory.getLogger(ClassTest.class);
    @Autowired
    public StudentService studentService;
    @Autowired
    public ClassService classService;
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
        savedStudent = studentService.addStudent(new Student("Ad", "O'Ding", UUID.randomUUID().toString(), false));
        additionalStudent = studentService.addStudent(new Student("Rem", "O'Ving", UUID.randomUUID().toString(), false));
    }

    @AfterEach
    public void teardown() {
        studentRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void assignStudentToClassTest() {
        SimpleClassDTO studentAddedClass = addStudentToClass(savedStudent);
        assertEquals(1, studentAddedClass.getClassStudents().size());
    }


    @Test
    @Order(2)
    public void moveStudentToOtherClassTest() {
        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(movedToClassId);
        if (schoolClassOptional.isPresent()) {
            SchoolClass destinationClassForStudent = schoolClassOptional.get();

            int preMoveSize = destinationClassForStudent.getClassStudents().size();
            moveStudentToOtherClass(additionalStudent);
            SchoolClass movedStudentClassAfterUpdate = fetchUpdatedObject(destinationClassForStudent.getId());
            int postMoveSize = movedStudentClassAfterUpdate.getClassStudents().size();

            assertTrue(preMoveSize < postMoveSize);
        } else {
            throw new IllegalArgumentException("School class was not properly added during database warmup!");
        }


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

    private SimpleClassDTO addStudentToClass(Student student) {
        return classService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), primaryClassId, ClassAction.ADD));
    }

    private SimpleClassDTO moveStudentToOtherClass(Student student) {
//        addStudentToClass(student);
        return classService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), movedToClassId, ClassAction.MOVE));
    }

    private SimpleClassDTO removeStudentFromClass(Student student) {
        return classService.studentToClassAction(new ExistingStudentToClassDTO(student.getCode(), primaryClassId, ClassAction.REMOVE));
    }
}
