package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.entity.Grade;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.entity.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InputStudentsFromTextFileService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ClassService classService;
    private final ApplicationConfig applicationConfig;
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromTextFileService.class);
    private final Environment environment;
    private final Random randomizer = new Random();


    public InputStudentsFromTextFileService(StudentRepository studentRepository, SubjectRepository subjectRepository, GradeRepository gradeRepository, SchoolClassRepository schoolClassRepository, ClassService classService, ApplicationConfig applicationConfig, Environment environment) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.classService = classService;
        this.applicationConfig = applicationConfig;
        this.environment = environment;
    }

    public List<Student> addStudents(MultipartFile studentsFile) {
        log.info("Adding students from file..");
        readAndSaveStudentsFromFile(studentsFile);
        populateStudentsWithGradesBasedOnActiveProfile();
        return studentRepository.findAll();
    }

    private void readAndSaveStudentsFromFile(MultipartFile studentsFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(studentsFile.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineParts = line.split(" ");

                if (lineParts.length >= applicationConfig.getMinimumStudentTags()) {
                    Student currentStudent = saveStudentFromReaderLine(lineParts);
                    SchoolClass currentRandomClass = assignStudentToRandomClass(currentStudent);

                    schoolClassRepository.save(currentRandomClass);
                } else {
                    log.warn("Improper student record format, adding to database omitted!");
                    log.warn("Line was: {}", line);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Student saveStudentFromReaderLine(String[] lineParts) {
        String firstName = lineParts[1];
        String lastName = lineParts[2];
        LocalDate birtDate = LocalDate.parse(lineParts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return studentRepository.save(new Student(firstName, lastName, UUID.randomUUID().toString(), birtDate, false));
    }

    private SchoolClass assignStudentToRandomClass(Student currentStudent) {
        Random randomizer = new Random();
        List<SchoolClass> classes = schoolClassRepository.findAll();
        int classId = randomizer.nextInt(classes.size());

        SchoolClass currentRandomClass = classes.get(classId);
        if (classService.classFull(currentRandomClass)) {
            log.info("Max size class reached, new class needed!");
            currentRandomClass = classService.getOtherExistingClass();
        }

        if (classService.assignStudent2Class(currentRandomClass, currentStudent)) {
            currentStudent.setAssigned(true);
            log.info("Student added: {} to class: {}", currentStudent, currentRandomClass.className());
        } else {
            log.error("Error adding student: {} to class: {}", currentStudent, currentRandomClass.className());
        }
        return currentRandomClass;

    }

    private void populateStudentsWithGradesBasedOnActiveProfile() {
        if (isActiveProfile("devel")) {
            log.info("Devel profile active, populating on random student added from file with some random grades");
            fillStudentsWithRandomizedGrades();
        } else {
            log.debug("Not populating added students with randomized grades");
        }
    }

    private boolean isActiveProfile(String develProfile) {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase(develProfile)) {
                return true;
            }
        }
        return false;
    }

    private void fillStudentsWithRandomizedGrades() {
        int studentsCount = studentRepository.findAll().size();
        log.debug("Students found: {}", studentsCount);
        int subjectsCount = subjectRepository.findAll().size();
        log.debug("Subjects found: {}", subjectsCount);

        int numberOfRecords = applicationConfig.getGradesToAdd();

        log.info("Populating students with {} example grades by random..", numberOfRecords);
        double progress = 0;
        long eachTenthPartOfLoopTimeStart = System.currentTimeMillis();
        for (int i = 0; i < numberOfRecords; i++) {
            if (progressEveryTenPercent(progress, numberOfRecords)) {
                eachTenthPartOfLoopTimeStart = logProgressAndUpdateDuration(progress, numberOfRecords, eachTenthPartOfLoopTimeStart);
            }

            saveRandomGradeForRandomStudent(studentsCount);
            progress++;
        }
        log.info("Done!");
    }

    private boolean progressEveryTenPercent(double progress, int numberOfRecords) {
        int divider = numberOfRecords / 10;
        return progress % divider == 0 && progress != 0;
    }

    private long logProgressAndUpdateDuration(double progress, int numberOfRecords, long startTime) {
        double percentage = (progress / numberOfRecords) * 100;
        log.info("Progress: {}%", String.format("%.0f", percentage));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("This took {}s", String.format("%.2f", ((double) duration / 1000)));
        return System.currentTimeMillis();
    }

    private void saveRandomGradeForRandomStudent(int studentsCount) {
        try {
            Student randomStudent = findRandomStudent(zeroExclusiveRandomValue(studentsCount));
            long associatedWithStudentRandomSubjectId = findRandomSubjectIdForStudent(randomStudent);
            List<Integer> availableGrades = applicationConfig.getAvailableGrades();

            Optional<Subject> optionalRandomStudentSubject = subjectRepository.findById(associatedWithStudentRandomSubjectId);
            if (optionalRandomStudentSubject.isPresent()) {
                Subject randomStudentSubject = optionalRandomStudentSubject.get();
                gradeRepository.save(new Grade(
                                availableGrades.get(randomizer.nextInt(availableGrades.size())),
                                randomStudent,
                                randomStudentSubject
                        )
                );
            } else {
                throw new IllegalArgumentException("No subjects found for student: " + randomStudent);
            }
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
        }
    }

    private Student findRandomStudent(Long random) {
        Optional<Student> student = studentRepository.findById(random);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new IllegalArgumentException("Student not present upon application warmup!");
        }
    }

    private long zeroExclusiveRandomValue(long range) {
        return randomizer.nextLong(range) + 1;
    }

    private long findRandomSubjectIdForStudent(Student randomStudent) {
        List<Long> subjectsIdsStudentBelongTo = schoolClassRepository.findStudentClassSubjects(randomStudent.getCode());
        return subjectsIdsStudentBelongTo.get(randomizer.nextInt(subjectsIdsStudentBelongTo.size()));
    }
}
