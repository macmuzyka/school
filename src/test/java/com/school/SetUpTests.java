package com.school;

import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.StudentRepository;
import com.schoolmodel.model.School;
import com.schoolmodel.model.SchoolClass;
import com.schoolmodel.model.Student;
import com.schoolmodel.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
public class SetUpTests {
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
        SchoolClass class1 = new SchoolClass(classOne, subjects.stream().map(Subject::new).toList());
        SchoolClass class2 = new SchoolClass(classTwo, subjects.stream().map(Subject::new).toList());
        SchoolClass class3 = new SchoolClass(classThree, subjects.stream().map(Subject::new).toList());

        ArrayList<SchoolClass> classes = new ArrayList<>();
        classes.add(class1);
        classes.add(class2);
        classes.add(class3);
        schoolRepository.save(new School("SCHOOL ONE", classes));

        savedStudent = studentRepository.save(new Student("John", "O'Connor", UUID.randomUUID().toString()));
    }
}
