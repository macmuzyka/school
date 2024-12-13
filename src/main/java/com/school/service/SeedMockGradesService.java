package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.model.statistics.ProgressRecord;
import com.school.repository.GradeRepository;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.school.model.entity.Grade;
import com.school.model.entity.Student;
import com.school.model.entity.Subject;
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

    //TODO: think about rewriting this class to kotlin service just to simplify and reduce code
    public SeedMockGradesService(StudentRepository studentRepository, SubjectRepository subjectRepository, SchoolClassRepository schoolClassRepository, GradeRepository gradeRepository, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.gradeRepository = gradeRepository;
        this.applicationConfig = applicationConfig;
    }

    public void seedStudentsWithRandomizedGrades() {
        int totalGrades = applicationConfig.getGradesToAdd();
        log.info("Populating students with {} example grades by random..", totalGrades);

        seedGradesAmongStudents(totalGrades);
    }

    public void seedGradesAmongStudents(int totalGrades) {
        long tenthPartLoopTime = startNewLoopTime();
        for (int record = 0; record < totalGrades; record++) {
            if (progressIsTenthPart(record)) {
                saveCurrentProgressRecord(record, tenthPartLoopTime);
                tenthPartLoopTime = startNewLoopTime();
            }

            saveRandomGradeForRandomStudent();
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

    private void saveRandomGradeForRandomStudent() {
        int randomStudentRange = studentRepository.findAll().size();
        try {
            Student randomStudent = findRandomStudent(zeroExclusiveRandomValueForId(randomStudentRange));
            List<Subject> subjectsOfRandomStudent = randomStudent.getSchoolClass().getClassSubjects();
            long randomlyChosenSubjectId = subjectsOfRandomStudent.get(randomizer.nextInt(subjectsOfRandomStudent.size())).getId();

            List<Integer> availableGrades = applicationConfig.getAvailableGrades();

            Optional<Subject> optionalRandomStudentSubject = subjectRepository.findById(randomlyChosenSubjectId);
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
            e.printStackTrace();
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

    private long zeroExclusiveRandomValueForId(long range) {
        return randomizer.nextLong(range) + 1;
    }

    public boolean notPopulatedYet() {
        return gradeRepository.findAll().isEmpty();
    }
}
