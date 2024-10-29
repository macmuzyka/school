package com.school.service;

import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.schoolmodel.model.dto.StudentDTO;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Student;
import jakarta.transaction.Transactional;
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

    public List<StudentDTO> getAllStudents(Boolean assignedParam) {
        log.info("Getting all students info..");
        return findStudentsBasedOnPassedParam(assignedParam);
    }

    private List<StudentDTO> findStudentsBasedOnPassedParam(Boolean assignedParam) {
        List<Student> foundStudents;
        if (assignedParam == null) {
            foundStudents = studentRepository.findAll();
        } else {
            foundStudents = studentRepository.findAllByAssigned(assignedParam);
        }
        return foundStudents.stream().map(StudentDTO::new).toList();
    }

    @Transactional
    public Student addStudentAndAssignToClass(NewStudentWithClassDTO studentDto) {
        log.info("Adding student: [{}] and assigning it to class: [{}]", studentDto, studentDto.getClassName());
        Student savedStudent = studentRepository.save(
                new Student(studentDto.getName(), studentDto.getSurname(), UUID.randomUUID().toString(), studentDto.getBirthDate(), true));
        String className = studentDto.getClassName();

        Optional<SchoolClass> schoolClass = schoolClassRepository.findSchoolClassByName(className);
        if (schoolClass.isPresent()) {
            SchoolClass existingClass = schoolClass.get();
            if (existingClass.getClassStudents().size() < Integer.parseInt(classMaxSize)) {
                existingClass.getClassStudents().add(savedStudent);
                schoolClassRepository.save(existingClass);
            } else {
                throw new IllegalArgumentException("Class " + studentDto.getClassName() + " reached max size, find other class to assign student to!");
            }
        } else {
            throw new IllegalArgumentException("No such class " + className + " found to assign student to!");
        }
        return savedStudent;
    }

    public Student addStudent(Student student) {
        log.info("Adding student: [{}]", student);
        return studentRepository.save(student);
    }

    public String deleteStudent(long id) {
        Optional<Student> studentToDelete = studentRepository.findById(id);
        if (studentToDelete.isPresent()) {
            Student student = studentToDelete.get();
            log.info("Student {} {} with id [{}] to delete found:", student.getName(), student.getSurname(), student.getId());
            studentRepository.delete(studentToDelete.get());
            return "Student with id: " + student.getId() + " deleted!";
        } else {
            return "Student with id: " + id + " to delete not found!";
        }
    }

    public Student updateStudent(Student updatedStudent) {
        Optional<Student> studentToUpdate = studentRepository.findById(updatedStudent.getId());
        if (studentToUpdate.isPresent()) {
            Student toSave = studentToUpdate.get();
            toSave.setName(updatedStudent.getName());
            toSave.setSurname(updatedStudent.getSurname());
            Student savedStudent = studentRepository.save(toSave);
            log.info("Student before update: {}", toSave);
            log.info("Student after update: {}", savedStudent);
            return savedStudent;
        } else {
            throw new IllegalArgumentException("Student with id: " + updatedStudent.getId() + " does not exist!");
        }
    }
}
