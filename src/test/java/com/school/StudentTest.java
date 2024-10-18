package com.school;

import com.schoolmodel.model.SchoolClass;
import com.schoolmodel.model.Student;
import com.schoolmodel.model.Subject;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.school.utils.ValidationUtils.codeIsValidUUID;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("devel")
public class StudentTest extends SetUpTests {
    private final Logger log = LoggerFactory.getLogger(StudentTest.class);

    @Test
    public void createStudentTest() {
        assertEquals(1, studentRepository.findAll().size());
        assertEquals("John", savedStudent.getName());
        assertEquals("O'Connor", savedStudent.getSurname());
        assertTrue(codeIsValidUUID(savedStudent.getCode()));
        assertNull(savedStudent.getStudentGrades());
    }

    @Test
    public void assignStudentToClassTest() {
        Optional<SchoolClass> optionalPreUpdateClass = schoolClassRepository.findSchoolClassByName(classOne);
        if (optionalPreUpdateClass.isPresent()) {
            SchoolClass preUpdate = optionalPreUpdateClass.get();
            int preUpdateSize = preUpdate.getClassStudents().size();
            assertEquals(0, preUpdate.getClassStudents().size());
            preUpdate.getClassStudents().add(new Student("John", "O'Connor", UUID.randomUUID().toString()));
            if (schoolClassRepository.findSchoolClassByName(classOne).isPresent()) {
                SchoolClass postUpdate = schoolClassRepository.findSchoolClassByName(classOne).get();
                int postUpdateSize = postUpdate.getClassStudents().size();
                assertEquals(1, postUpdate.getClassStudents().size());
                assertNotEquals(preUpdateSize, postUpdateSize);
            }
        }
    }
}
