package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.model.statistics.ProgressRecord;
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

import java.util.*;

import static com.school.service.ProgressRecordExportService.progressRecords;
import static com.school.service.ProgressRecordExportService.recordsReadyToExport;

@Service
public class SeedMockGradesService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final GradeRepository gradeRepository;
    private final ApplicationConfig applicationConfig;
    private final Random randomizer = new Random();
    private final Logger log = LoggerFactory.getLogger(SeedMockGradesService.class);

    public SeedMockGradesService(StudentRepository studentRepository, SubjectRepository subjectRepository, SchoolClassRepository schoolClassRepository, GradeRepository gradeRepository, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.gradeRepository = gradeRepository;
        this.applicationConfig = applicationConfig;
    }

    public void seedStudentsWithRandomizedGrades() {
        int studentRange = studentRepository.findAll().size();
        log.debug("Students found: {}", studentRange);

        int totalGrades = applicationConfig.getGradesToAdd();
        log.info("Populating students with {} example grades by random..", totalGrades);

        seedGradesAmongStudents(totalGrades, studentRange);
    }

    public void seedGradesAmongStudents(int totalGrades, int studentRange) {
        long tenthPartLoopTime = startNewLoopTime();
        for (int record = 0; record < totalGrades; record++) {
            if (progressIsTenthPart(record)) {
                saveCurrentProgressRecord(record, tenthPartLoopTime);
                tenthPartLoopTime = startNewLoopTime();
            }

            saveRandomGradeForRandomStudent(studentRange);
        }
        log.info("Populating with random grades done");
        recordsReadyToExport = true;
    }

    private long startNewLoopTime() {
        return System.currentTimeMillis();
    }

    private boolean progressIsTenthPart(int record) {
        int divider = calculateDivider();
        return progressIsExactlyTenthPathOfTotalRecords(record, divider) || isLastRecord(record);
    }

    private int calculateDivider() {
        return applicationConfig.getGradesToAdd() / 10;
    }

    private boolean progressIsExactlyTenthPathOfTotalRecords(double progress, int divider) {
        return progress % divider == 0;
    }

    private boolean isLastRecord(int record) {
        return (record + 1 == applicationConfig.getGradesToAdd());
    }

    private void saveCurrentProgressRecord(int record, long tenthPartLoopTime) {
        if (isLastRecord(record)) {
            record++;
        }
        ProgressRecord currentProgressRecord = prepareProgressRecord(record, tenthPartLoopTime);
        progressRecords.add(currentProgressRecord);
        logProgressRecord(currentProgressRecord);
    }

    private ProgressRecord prepareProgressRecord(int record, long tenthPartLoopTime) {
        return new ProgressRecord(currentPercentageProgress(record), loopDurationInSeconds(tenthPartLoopTime));
    }

    private int currentPercentageProgress(int progress) {
        return (int) (((double) progress / applicationConfig.getGradesToAdd()) * 100);
    }

    private double loopDurationInSeconds(long previousLoopInMillis) {
        return ((double) System.currentTimeMillis() - (double) previousLoopInMillis) / 1000;
    }

    private void logProgressRecord(ProgressRecord currentProgressRecord) {
        log.info("Adding {}% of records took {}s", currentProgressRecord.getPercentageProgress(), currentProgressRecord.getDuration());
    }

    private void saveRandomGradeForRandomStudent(int randomStudentRange) {
        try {
            Student randomStudent = findRandomStudent(zeroExclusiveRandomValue(randomStudentRange));
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
