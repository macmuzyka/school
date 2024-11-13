package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.entity.Grade;
import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.entity.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MockDataGradeSeederService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final GradeRepository gradeRepository;
    private final ApplicationConfig applicationConfig;
    private final Random randomizer = new Random();
    private final Logger log = LoggerFactory.getLogger(MockDataGradeSeederService.class);

    public MockDataGradeSeederService(StudentRepository studentRepository, SubjectRepository subjectRepository, SchoolClassRepository schoolClassRepository, GradeRepository gradeRepository, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.gradeRepository = gradeRepository;
        this.applicationConfig = applicationConfig;
    }

    public void fillStudentsWithRandomizedGrades() {
        int studentsCount = studentRepository.findAll().size();
        log.debug("Students found: {}", studentsCount);

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
        log.info("Progress: {}%", calculatePercentageProgress(progress, numberOfRecords));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("This took {}s", calculateTimeInSeconds(duration));
        return System.currentTimeMillis();
    }

    private String calculatePercentageProgress(double progress, int numberOfRecords) {
        double percentageProgress = (progress / numberOfRecords) * 100;
        return String.format("%.0f", percentageProgress);
    }

    private String calculateTimeInSeconds(long duration) {
        double calculatedTime = (double) duration / 1000;
        return String.format("%.2f", (calculatedTime));
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
