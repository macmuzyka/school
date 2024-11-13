package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.repository.StudentInsertErrorRepository;
import com.schoolmodel.model.dto.NewStudentWithClassDTO;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.schoolmodel.model.dto.StudentDTO;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.entity.StudentInsertError;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final StudentInsertErrorRepository studentInsertErrorRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ApplicationConfig applicationConfig;

    public StudentService(StudentRepository studentRepository, StudentInsertErrorRepository studentInsertErrorRepository, SchoolClassRepository schoolClassRepository, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.studentInsertErrorRepository = studentInsertErrorRepository;
        this.schoolClassRepository = schoolClassRepository;
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

    public Student updateStudent(StudentDTO updatedStudent) {
        Optional<Student> studentToUpdate = studentRepository.findById(updatedStudent.getId());
        if (studentToUpdate.isPresent()) {
            Student toSave = studentToUpdate.get();
            log.info("Student before update: {}", toSave);
            toSave.setName(updatedStudent.getName());
            toSave.setSurname(updatedStudent.getSurname());
            Student savedStudent = studentRepository.save(toSave);
            log.info("Student after update: {}", savedStudent);
            return savedStudent;
        } else {
            throw new IllegalArgumentException("Student with id: " + updatedStudent.getId() + " does not exist!");
        }
    }

    public Student getStudent(long studentId) {
        Optional<Student> optStudent = studentRepository.findById(studentId);
        if (optStudent.isPresent()) {
            return optStudent.get();
        } else {
            throw new IllegalArgumentException("No student with id " + studentId + " found!");
        }
    }

    public List<StudentInsertError> getInputErrorStudents() {
        return studentInsertErrorRepository.findAll();
    }

    @Transactional
    public Student correctStudent(StudentDTO correctedStudent) {
        Optional<StudentInsertError> optionalStudent = studentInsertErrorRepository.findById(correctedStudent.getId());
        if (optionalStudent.isPresent()) {
            StudentInsertError toRemove = optionalStudent.get();
            Student corrected = prepareStudent(correctedStudent);
            return saveCorrectedAndRemoveStudentInsertError(corrected, toRemove);
        } else {
            throw new IllegalArgumentException("Error record student with id " + correctedStudent.getId() + " not found!");
        }
    }

    private Student prepareStudent(StudentDTO correctedStudent) {
        return new Student(correctedStudent.getName(),
                correctedStudent.getSurname(),
                correctedStudent.getIdentifier(),
                UUID.randomUUID().toString(),
                correctedStudent.getBirthDate(),
                false
        );
    }

    private Student saveCorrectedAndRemoveStudentInsertError(Student corrected, StudentInsertError toRemove) {
        Student savedAfterCorrection = studentRepository.save(corrected);
        log.info("Saved corrected student: {}", savedAfterCorrection);
        log.info("To remove student: {}", toRemove);
        studentInsertErrorRepository.delete(toRemove);
        return savedAfterCorrection;
    }
}
