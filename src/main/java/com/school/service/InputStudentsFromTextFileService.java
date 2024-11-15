package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.repository.*;
import com.schoolmodel.model.dto.StudentDTO;
import com.schoolmodel.model.entity.*;
import com.schoolmodel.model.enums.InsertStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InputStudentsFromTextFileService {
    private final StudentRepository studentRepository;
    private final StudentInsertErrorRepository studentInsertErrorRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SeedMockGradesService seedMockGradesService;
    private final ClassService classService;
    private final EnvironmentService environmentService;
    private final ApplicationConfig applicationConfig;
    private final Map<InsertStatus, Set<StudentDTO>> insertions = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromTextFileService.class);


    public InputStudentsFromTextFileService(StudentRepository studentRepository,
                                            StudentInsertErrorRepository studentInsertErrorRepository,
                                            SchoolClassRepository schoolClassRepository,
                                            SeedMockGradesService seedMockGradesService, ClassService classService,
                                            ApplicationConfig applicationConfig,
                                            EnvironmentService environmentService
    ) {
        this.studentRepository = studentRepository;
        this.studentInsertErrorRepository = studentInsertErrorRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.seedMockGradesService = seedMockGradesService;
        this.classService = classService;
        this.applicationConfig = applicationConfig;
        this.environmentService = environmentService;
    }

    public List<Student> addStudents(MultipartFile studentsFile) {
        log.info("Adding students from file..");
        initializeAuxiliaryMap();
        readAndSaveStudentsFromFile(studentsFile);
        populateStudentsWithGradesBasedOnActiveProfile();
        clearAuxiliaryMap();
        return studentRepository.findAll();
    }

    private void readAndSaveStudentsFromFile(MultipartFile studentsFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(studentsFile.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                if (lineHasProperNumberOfTags(parts)) {
                    Student currentStudent = buildStudentFromReaderLine(parts);
                    SchoolClass currentRandomClass = classService.assignStudentToFirstOpenClass(currentStudent);
                    schoolClassRepository.save(currentRandomClass);
                } else {
                    log.warn("Improper student record format, error line: {}", line);
                    StudentInsertError errorStudent = buildStudentFromImproperReadLine(parts);
                    studentInsertErrorRepository.save(errorStudent);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean lineHasProperNumberOfTags(String[] lineParts) {
        return lineParts.length == applicationConfig.getMinimumStudentTags();
    }

    private StudentInsertError buildStudentFromImproperReadLine(String[] lineParts) {
        return new StudentInsertError(
                new Student("", "", "", "", LocalDate.now().minus(Period.ofYears(1000)), false),
                "Error line in file: " + String.join(",", lineParts),
                "Bad format in student file"
        );
    }

    //TODO: refactor code, and elevate to separate service
    private Student buildStudentFromReaderLine(String[] lineParts) {
        String firstName = lineParts[1];
        String lastName = lineParts[2];
        LocalDate birtDate = LocalDate.parse(lineParts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String identifier = lineParts[4];
        Student toSave = new Student(firstName, lastName, identifier, UUID.randomUUID().toString(), birtDate, false);
        if (insertions.get(InsertStatus.SUCCESS).add(new StudentDTO(toSave))) {
            return studentRepository.save(toSave);
        } else {
            log.warn("Student with identifier {} is already added, verify correctness of its value in uploaded students file!", toSave.getIdentifier());
            log.warn("Attempting to save invalid record..");
            StudentInsertError studentInsertError = new StudentInsertError(toSave, "Duplicated identifier", "Insert error");
            if (insertions.get(InsertStatus.ERROR).add(new StudentDTO(studentInsertError))) {
                studentInsertErrorRepository.save(studentInsertError);
                log.warn("Invalid record saved to be repaired in future");
            } else {
                log.warn("Student with duplicated {} identifier already saved, omitting record", studentInsertError.getIdentifier());
            }
            return null;
        }
    }

    private void populateStudentsWithGradesBasedOnActiveProfile() {
        if (environmentService.profileOtherThanDefaultIsActive()) {
            log.info("Other than default profile active thus for performance measure populating on random student added from file with some random grades");
            seedMockGradesService.seedStudentsWithRandomizedGrades();
        } else {
            log.debug("Not populating added students with randomized grades");
        }
    }

    private void initializeAuxiliaryMap() {
        insertions.put(InsertStatus.SUCCESS, studentRepository.findAll().stream().map(StudentDTO::new).collect(Collectors.toSet()));
        insertions.put(InsertStatus.ERROR, studentInsertErrorRepository.findAll().stream().map(StudentDTO::new).collect(Collectors.toSet()));
    }

    private void clearAuxiliaryMap() {
        this.insertions.get(InsertStatus.SUCCESS).clear();
        this.insertions.get(InsertStatus.ERROR).clear();
    }
}