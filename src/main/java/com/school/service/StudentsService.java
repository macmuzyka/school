package com.school.service;

import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.*;
import com.school.repository.StudentRepository;
import com.schoolmodel.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentsService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final SchoolClassRepository schoolClassRepository;
    private static final Logger log = LoggerFactory.getLogger(StudentsService.class);
    @Value("${grades.to.add}")
    private String gradeRecords2Add;
    @Value("${divider}")
    private String divider;


    public StudentsService(StudentRepository studentRepository, SubjectRepository subjectRepository, GradeRepository gradeRepository, SchoolClassRepository schoolClassRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

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

                    Student currentStudent = studentRepository.save(new Student(firstName, lastName, UUID.randomUUID().toString()));
                    int classId = randClass.nextInt(classRange);
                    log.debug("Randomized class ID: {}", classId);
                    SchoolClass currentRandomizedClass = classes.get(classId);
                    if (currentRandomizedClass.assignStudent2Class(currentStudent)) {
                        log.info("Student added: {}\nto class: {}", currentStudent, currentRandomizedClass.className());
                    }
                    //TODO: check if it works without below saving to repository!
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

        fillStudentsWithRandomizedGrades();
        return studentRepository.findAll();
    }

    public List<SubjectGradesDTO> getSubjectGradesForStudent(Long studentId) {
        log.info("Getting grades for students grouped by subjects..");
        List<Object[]> results = gradeRepository.findAllGradesGroupedBySubjectForStudent(studentId);
        try {
            return results.stream().map(result -> {
                String studentName = (String) result[0];
                String subject = (String) result[1];
                String grades = (String) result[2];
                BigDecimal average = (BigDecimal) result[3];
                return new SubjectGradesDTO(studentName, subject, grades, average);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    private void fillStudentsWithRandomizedGrades() {
        Random rand = new Random();
        int students = studentRepository.findAll().size();
        log.debug("Students found: {}", students);
        int subjects = subjectRepository.findAll().size();
        log.debug("Subjects found: {}", subjects);
        int numberOfRecords = Integer.parseInt(gradeRecords2Add);
        log.info("Populating students with {} example grades by random..", numberOfRecords);
        double progress = 0;
        for (int i = 0; i < numberOfRecords; i++) {
            if (progress % Integer.parseInt(divider) == 0 && progress != 0) {
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

    public List<Student> getAllStudents() {
        log.info("Getting all students info..");
        return studentRepository.findAll();
    }

    public Grade addGrade(Grade grade) {
        log.info("Adding grade {} to student {} to subject {}", grade.getGradeValue(), grade.getStudent(), grade.getSubject());
        return gradeRepository.save(grade);
    }

    public Grade addGrade(int grade, String subject, String studentCode) {
        log.info("Adding grade {} to student {} to subject {}", grade, studentCode, subject);
        Optional<Subject> subjectFound = subjectRepository.findSubjectByName(subject);
        if (subjectFound.isPresent()) {
            Optional<Student> studentFound = studentRepository.findStudentByCode(studentCode);
            if (studentFound.isPresent()) {
                log.info(studentFound.get().getName() + " " + studentFound.get().getSurname() + " found based on code!");
                if (grade > 0 && grade < 6) {
                    return gradeRepository.save(new Grade(
                            grade,
                            studentFound.get(),
                            subjectFound.get())
                    );
                } else {
                    String message = "Assigning " + grade + " blocked as it is improver value!";
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }
            } else {
                String message = "Cannot find " + studentCode + " student code to assign grade to!";
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        } else {
            String message = "Cannot find " + subject + " subject to assign grade to!";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
