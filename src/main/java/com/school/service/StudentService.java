package com.school.service;

import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.schoolmodel.model.SchoolClass;
import com.schoolmodel.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(StudentService.class);
    @Value("${class.max.students}")
    private String classMaxSize;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public StudentService(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    public List<Student> getAllStudents() {
        log.info("Getting all students info..");
        return studentRepository.findAll();
    }

    public Student addStudentAndAssignToClass(NewStudentWithClassDTO studentDto) {
        log.info("Adding student: [{}] and assigning it to class: [{}]", studentDto, studentDto.getClassName());
        Student student = new Student(studentDto.getName(), studentDto.getSurname(), UUID.randomUUID().toString());
        String className = studentDto.getClassName();
        Optional<SchoolClass> schoolClass = schoolClassRepository.findSchoolClassByName(className);
        if (schoolClass.isPresent()) {
            SchoolClass existingClass = schoolClass.get();
            if (existingClass.getClassStudents().size() < Integer.parseInt(classMaxSize)) {
                existingClass.getClassStudents().add(student);
                schoolClassRepository.save(existingClass);
                log.info("saved via school class repository!");
            } else {
                throw new IllegalArgumentException("Class " + studentDto.getClassName() + " reached max size, find other class to assign student to!");
            }
        } else {
            throw new IllegalArgumentException("No such class " + className + " found to assign student to!");
        }
        return student;
    }

    public Student addStudent(Student student) {
        log.info("Adding student: [{}]", student);
        return studentRepository.save(student);
    }
}
