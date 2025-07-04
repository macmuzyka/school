package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.model.entity.Grade;
import com.school.model.entity.Student;
import com.school.model.entity.Subject;
import com.school.model.statistics.ProgressRecord;
import com.school.repository.GradeRepository;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.school.service.ProgressRecordExportService.progressRecords;

@Component
public class SeedGradeWorker {
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ApplicationConfig applicationConfig;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final Logger log = LoggerFactory.getLogger(SeedGradeWorker.class);
    private final Random randomizer = new Random();

    public SeedGradeWorker(GradeRepository gradeRepository,
                           SubjectRepository subjectRepository,
                           StudentRepository studentRepository,
                           ApplicationConfig applicationConfig,
                           SendNotificationToFrontendService sendNotificationToFrontendService) {
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.applicationConfig = applicationConfig;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
    }

    @Transactional
    public void seedChunk(List<Long> studentIds, int chunkSize, AtomicInteger completed, Set<Integer> loggedSteps, long startTime) {
        for (int j = 0; j < chunkSize; j++) {
            Long studentId = studentIds.get(randomizer.nextInt(studentIds.size()));
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Student with id" + studentId + " not found!"));
            log.debug("Random student found: {}", student.toString());

            saveRandomGradeForRandomStudent(student);
            int current = completed.incrementAndGet();

            if (current % chunkSize == 0 && loggedSteps.add(current)) {
                saveCurrentProgressRecord(current, startTime);
            }
        }
    }

    private void saveRandomGradeForRandomStudent(Student randomStudent) {
        try {
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
            log.error("Exception while saving random grade to random student: ", e);
        }
    }

    private String randomGradeType() {
        List<String> gradeTypes = applicationConfig.getGradeTypes().stream().toList();
        return gradeTypes.get(randomizer.nextInt(gradeTypes.size()));
    }

    private void saveCurrentProgressRecord(int record, long tenthPartLoopTime) {
        if (isLastRecord(record)) {
            record++;
        }
        ProgressRecord currentProgressRecord = prepareProgressRecord(record, tenthPartLoopTime);
        progressRecords.add(currentProgressRecord);
        logProgressRecord(currentProgressRecord);
    }

    private boolean isLastRecord(int record) {
        return (record + 1 == applicationConfig.getGradesToAdd());
    }

    private ProgressRecord prepareProgressRecord(int record, long tenthPartLoopTime) {
        return new ProgressRecord(currentPercentageProgress(record), loopDurationInSeconds(tenthPartLoopTime));
    }

    private double loopDurationInSeconds(long previousLoopInMillis) {
        return ((double) System.currentTimeMillis() - (double) previousLoopInMillis) / 1000;
    }

    private void logProgressRecord(ProgressRecord currentProgressRecord) {
        log.info("Adding {}% of records took {}s", currentProgressRecord.getPercentageProgress(), currentProgressRecord.getDuration());
        sendNotificationToFrontendService.notifyFrontendAboutSeedingProgress(currentProgressRecord);
    }

    private int currentPercentageProgress(int progress) {
        return (int) (((double) progress / applicationConfig.getGradesToAdd()) * 100);
    }
}
