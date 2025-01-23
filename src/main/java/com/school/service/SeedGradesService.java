package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.model.statistics.ProgressRecord;
import com.school.repository.GradeRepository;
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
public class SeedGradesService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final ApplicationConfig applicationConfig;
    private final Random randomizer = new Random();
    private final Logger log = LoggerFactory.getLogger(SeedGradesService.class);

    private List<Long> studentIds;

    public SeedGradesService(StudentRepository studentRepository, SubjectRepository subjectRepository, GradeRepository gradeRepository, SendNotificationToFrontendService sendNotificationToFrontendService, ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
        this.applicationConfig = applicationConfig;
    }

    public void seedStudentsWithRandomizedGrades() {
        int totalGrades = applicationConfig.getGradesToAdd();
        log.info("Populating students with {} example grades by random..", totalGrades);
        sendNotificationToFrontendService.notifyFrontendAboutSeedingGradesStatus("Seeding " + totalGrades + " grades randomly " +
                "among added students, this might take a while");
        seedGradesAmongStudents(totalGrades);
    }

    private void seedGradesAmongStudents(int totalGrades) {
        long tenthPartLoopTime = startNewLoopTime();
        studentIds = studentRepository.findAllIds();
        for (int record = 0; record < totalGrades; record++) {
            if (progressIsTenthPart(record)) {
                saveCurrentProgressRecord(record, tenthPartLoopTime);
                tenthPartLoopTime = startNewLoopTime();
            }

            saveRandomGradeForRandomStudent();
        }
        log.info("Populating with random grades done");
        sendNotificationToFrontendService.notifyFrontendAboutSeedingGradesStatus("Seeding Done!");
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
        sendNotificationToFrontendService.notifyFrontendAboutSeedingProgress(currentProgressRecord);
    }

    private void saveRandomGradeForRandomStudent() {
        try {
            Student randomStudent = findRandomStudent();
            if (randomStudent.getSchoolClass() != null) {
                Set<Subject> subjectsOfRandomStudent = randomStudent.getSchoolClass().getClassSubjects();
                long randomlyChosenSubjectId = subjectsOfRandomStudent.stream()
                        .toList()
                        .get(randomizer.nextInt(subjectsOfRandomStudent.size())).getId();

                List<Integer> availableGrades = applicationConfig.getAvailableGrades();

                Optional<Subject> optionalRandomStudentSubject = subjectRepository.findById(randomlyChosenSubjectId);
                if (optionalRandomStudentSubject.isPresent()) {
                    Subject randomStudentSubject = optionalRandomStudentSubject.get();
                    gradeRepository.save(new Grade(
                                    availableGrades.get(randomizer.nextInt(availableGrades.size())),
                                    randomStudent,
                                    randomStudentSubject,
                                    "Created randomly by seeder",
                                    randomGradeType()
                            )
                    );
                } else {
                    throw new IllegalArgumentException("No subjects found for student: " + randomStudent);
                }
            } else {
                log.info("Student with identifier {} unassigned, skipping seeding grade in this student record", randomStudent.getIdentifier());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private String randomGradeType() {
        List<String> gradeTypes = applicationConfig.getGradeTypes().stream().toList();
        return gradeTypes.get(randomizer.nextInt(gradeTypes.size()));
    }

    private Student findRandomStudent() {
        Long randomStudentID = studentIds.get(randomizer.nextInt(studentIds.size()));
        Optional<Student> student = studentRepository.findById(randomStudentID);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new IllegalArgumentException("Student not present upon application warmup!");
        }
    }
}
