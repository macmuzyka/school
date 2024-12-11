package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.model.FileToImport;
import com.school.model.OptionalRequestParams;
import com.school.model.dto.NewStudentWithClassDTO;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.model.dto.StudentDTO;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.Student;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
//TODO: clean & refactor code
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentsListFileProviderService studentsListFileProviderService;
    private final ApplicationConfig applicationConfig;

    public StudentService(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, StudentsListFileProviderService studentsListFileProviderService, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.studentsListFileProviderService = studentsListFileProviderService;
        this.applicationConfig = applicationConfig;
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
                new Student(studentDto.getName(),
                        studentDto.getSurname(),
                        studentDto.getIdentifier(),
                        UUID.randomUUID().toString(),
                        studentDto.getBirthDate(),
                        true
                )
        );
        String className = studentDto.getClassName();

        Optional<SchoolClass> schoolClass = schoolClassRepository.findSchoolClassByName(className);
        if (schoolClass.isPresent()) {
            SchoolClass existingClass = schoolClass.get();
            if (existingClass.getClassStudents().size() < applicationConfig.getClassMaxSize()) {
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

    public Student addStudent(StudentDTO student) {
        Student properStudentObject = prepareStudentFromDTO(student);
        log.info("Adding student: [{}]", properStudentObject);
        return studentRepository.save(properStudentObject);
    }

    private Student prepareStudentFromDTO(StudentDTO student) {
        return new Student(student.getName(), student.getSurname(), student.getIdentifier(), UUID.randomUUID().toString(), student.getBirthDate(), false);
    }

    public String deleteStudent(Long id) {
        Optional<Student> studentToDelete = studentRepository.findById(id);
        if (studentToDelete.isPresent()) {
            studentRepository.deleteById(id);
            return "Student with id " + id + " removed";
        } else {
            return "Student with id: " + id + " to delete not found!";
        }

    }

    public StudentDTO updateStudent(StudentDTO updatedStudent) {
        //TODO: fix form size
        Optional<Student> studentToUpdate = studentRepository.findById(updatedStudent.getId());
        if (studentToUpdate.isPresent()) {
            Student toSave = studentToUpdate.get();
            log.info("Student before update: {}", toSave);
            toSave.setName(updatedStudent.getName());
            toSave.setSurname(updatedStudent.getSurname());
            Student savedStudent = studentRepository.save(toSave);
            log.info("Student after update: {}", savedStudent);
            return new StudentDTO(savedStudent);
        } else {
            throw new IllegalArgumentException("Student with id: " + updatedStudent.getId() + " does not exist!");
        }
    }

    public Student getStudent(Long studentId) {
        Optional<Student> optStudent = studentRepository.findById(studentId);
        if (optStudent.isPresent()) {
            return optStudent.get();
        } else {
            throw new IllegalArgumentException("No student with id " + studentId + " found!");
        }
    }


    public List<StudentDTO> findStudents(OptionalRequestParams params) {
        log.info("Received query params: {}", params);
        List<StudentDTO> studentsFound = studentRepository.findAllStudentsByParams(params.getId(), params.getName(), params.getSurname(), params.getIdentifier()).stream()
                .map(StudentDTO::new).toList();
        log.info("Retrieved students: {}", studentsFound);
        return studentsFound;
    }

    public FileToImport getFile() {
        return studentsListFileProviderService.getFile();
    }
}
