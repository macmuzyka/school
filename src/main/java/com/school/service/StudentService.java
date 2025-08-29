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
import com.school.service.fileproducers.StudentsListFileProviderService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//TODO: clean & refactor code
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentsListFileProviderService studentsListFileProviderService;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final ApplicationConfig applicationConfig;

    public StudentService(StudentRepository studentRepository,
                          SchoolClassRepository schoolClassRepository,
                          StudentsListFileProviderService studentsListFileProviderService,
                          SendNotificationToFrontendService sendNotificationToFrontendService,
                          ApplicationConfig applicationConfig
    ) {
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.studentsListFileProviderService = studentsListFileProviderService;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
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

    public Student addStudent(StudentDTO studentDTO) {
        try {
            log.info("Adding student: [{}]", studentDTO);
            if (identifierAlreadyExists(studentDTO.getIdentifier())) {
                throw new IllegalArgumentException("Student with identifier " + studentDTO.getIdentifier() + " already exists!");
            } else {
                Student student = prepareEntityObjectFromDTO(studentDTO);
                log.info("Prepared student: {}", student);
                Student saved = studentRepository.save(student);
                log.info("Saved student: {}", saved);
//                sendNotificationToFrontendService.notifyFrontendAboutAddedStudent("OK");
                return saved;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Student prepareEntityObjectFromDTO(StudentDTO student) {
        return new Student(student.getName(), student.getSurname(), student.getIdentifier(), student.getBirthDate(), false);
    }

    public String deleteStudent(Long id) {
        Optional<Student> studentToDelete = studentRepository.findById(id);
        if (studentToDelete.isPresent()) {
            studentRepository.deleteById(id);
            sendNotificationToFrontendService.notifyFrontendAboutStudentRemovalStatus("OK");
            return "Student with id " + id + " removed";
        } else {
            sendNotificationToFrontendService.notifyFrontendAboutStudentRemovalStatus("Student with id: " + id + " to delete not found!");
            return "Student with id: " + id + " to delete not found!";
        }
    }

    public StudentDTO updateStudent(StudentDTO incomingUpdatedStudent) {
        Optional<Student> studentToUpdate = studentRepository.findById(incomingUpdatedStudent.getId());
        if (studentToUpdate.isPresent()) {
            Student foundStudentToUpdate = studentToUpdate.get();
            String incomingIdentifier = incomingUpdatedStudent.getIdentifier().trim();
            String existingIdentifier = foundStudentToUpdate.getIdentifier().trim();
            if (identifierChanged(incomingIdentifier, existingIdentifier)
                    && identifierAlreadyExists(incomingIdentifier)) {
                throw new IllegalArgumentException("Student with identifier " + incomingUpdatedStudent.getIdentifier()
                        + " already exists!");
            } else {
                log.info("Student before update: {}", foundStudentToUpdate);
                foundStudentToUpdate.setName(incomingUpdatedStudent.getName());
                foundStudentToUpdate.setSurname(incomingUpdatedStudent.getSurname());
                Student savedStudent = studentRepository.save(foundStudentToUpdate);
                log.info("Student after update: {}", savedStudent);
                sendNotificationToFrontendService.notifyFrontendAboutStudentUpdateStatus("OK");
                return new StudentDTO(savedStudent);
            }
        } else {
            throw new IllegalArgumentException("Student with id: " + incomingUpdatedStudent.getId() + " does not exist!");
        }
    }

    private Boolean identifierChanged(String incomingValue, String existingValue) {
        return !incomingValue.equals(existingValue);
    }

    public Boolean identifierAlreadyExists(String incomingIdentifier) {
        return studentRepository.findAll().stream().map(Student::getIdentifier).collect(Collectors.toSet())
                .contains(incomingIdentifier);
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
        List<StudentDTO> studentsFound = studentRepository
                .findAllStudentsByParams(params.getId(), params.getName(), params.getSurname(), params.getIdentifier()).stream()
                .map(StudentDTO::new).toList();
        log.info("Retrieved students: {}", studentsFound);
        return studentsFound;
    }

    public FileToImport importFile() {
        return studentsListFileProviderService.importFile(null);
    }
}
