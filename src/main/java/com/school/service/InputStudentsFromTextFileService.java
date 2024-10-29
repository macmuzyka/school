package com.school.service;

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
import org.springframework.beans.factory.annotation.Value;
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
    private static final Logger log = LoggerFactory.getLogger(InputStudentsFromTextFileService.class);
    @Value("${grades.to.add}")
    private String gradeRecords2Add;
    private final Environment environment;


    public InputStudentsFromTextFileService(StudentRepository studentRepository, SubjectRepository subjectRepository, GradeRepository gradeRepository, SchoolClassRepository schoolClassRepository, Environment environment) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.environment = environment;
    }

    //TODO: change logic so classes are automatically added when class max size is exceeded!
    public List<Student> addStudents(MultipartFile studentsFile) {
        log.info("Adding students from file..");
        List<SchoolClass> classes = schoolClassRepository.findAll();
        int classRange = classes.size();
        Random randClass = new Random();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(studentsFile.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineParts = line.split(" ");

                if (lineParts.length >= 3) {
                    String firstName = lineParts[1];
                    String lastName = lineParts[2];
                    LocalDate birtDate = LocalDate.parse(lineParts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    Student currentStudent = studentRepository.save(new Student(firstName, lastName, UUID.randomUUID().toString(), birtDate, true));
                    int classId = randClass.nextInt(classRange);
                    log.debug("Randomized class ID: {}", classId);
                    SchoolClass currentRandomizedClass = classes.get(classId);
                    if (currentRandomizedClass.assignStudent2Class(currentStudent)) {
                        log.info("Student added: {}\nto class: {}", currentStudent, currentRandomizedClass.className());
                    }
                    schoolClassRepository.save(currentRandomizedClass);
                } else {
                    log.warn("Improper student record format, adding to database omitted!");
                    log.warn("Line was: {}", line);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
        if (isActiveProfile("devel")) {
            log.info("Devel profile active, populating on random student added from file                                   with some random grades");
            fillStudentsWithRandomizedGrades();
        } else {
            log.debug("Not populating added students with randomized grades");
        }
        return studentRepository.findAll();
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
        Random rand = new Random();
        int students = studentRepository.findAll().size();
        log.debug("Students found: {}", students);
        int subjects = subjectRepository.findAll().size();
        log.debug("Subjects found: {}", subjects);
        int numberOfRecords = Integer.parseInt(gradeRecords2Add);
        int divider = numberOfRecords / 10;
        log.info("Populating students with {} example grades by random..", numberOfRecords);
        double progress = 0;
        for (int i = 0; i < numberOfRecords; i++) {
            if (progress % divider == 0 && progress != 0) {
                double percentage = (progress / numberOfRecords) * 100;
                log.info("Progress: {}%", String.format("%.0f", percentage));
            }
            gradeRepository.save(new Grade(
                    rand.nextInt(5) + 1,
                    randomStudent(rand.nextLong(students) + 1),
                    randomSubject(rand.nextLong(subjects) + 1))
            );
            progress++;
        }
        log.info("Done!");
    }

    private Student randomStudent(Long random) {
        Optional<Student> student = studentRepository.findById(random);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new IllegalArgumentException("Student not present upon application warmup!");
        }
    }

    private Subject randomSubject(Long random) {
        Optional<Subject> subject = subjectRepository.findById(random);
        if (subject.isPresent()) {
            return subject.get();
        } else {
            throw new IllegalArgumentException("Subject not present upon application warmup!");
        }
    }
}
