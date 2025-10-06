package com.school.service;

import com.school.configuration.ApplicationConfig;
import com.school.repository.sql.GradeRepository;
import com.school.repository.sql.StudentRepository;
import com.school.model.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.school.service.ProgressRecordExportService.recordsReadyToExport;

@Service
public class SeedGradesService {
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final SeedGradeWorker seedGradeWorker;
    private final SendNotificationToFrontendService sendNotificationToFrontendService;
    private final ApplicationConfig applicationConfig;

    private final Logger log = LoggerFactory.getLogger(SeedGradesService.class);

    public SeedGradesService(StudentRepository studentRepository,
                             GradeRepository gradeRepository,
                             SeedGradeWorker seedGradeWorker,
                             SendNotificationToFrontendService sendNotificationToFrontendService,
                             ApplicationConfig applicationConfig) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.seedGradeWorker = seedGradeWorker;
        this.sendNotificationToFrontendService = sendNotificationToFrontendService;
        this.applicationConfig = applicationConfig;
    }

    public void seedStudentsWithRandomizedGrades() throws InterruptedException {
        int totalGrades = applicationConfig.getGradesToAdd();
        log.info("Populating students with {} example grades by random..", totalGrades);
        sendNotificationToFrontendService.notifyFrontendAboutSeedingGradesStatus("Seeding " + totalGrades + " grades randomly " +
                "among added students, this might take a while");
        seedGradesAmongStudents(totalGrades);
    }

    private void seedGradesAmongStudents(int totalGrades) throws InterruptedException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);
        int chunkSize = totalGrades / availableProcessors;
        int totalChunks = totalGrades / chunkSize;

        try {
            AtomicInteger completed = new AtomicInteger(0);
            Set<Integer> loggedSteps = ConcurrentHashMap.newKeySet();
            List<Callable<Void>> tasks = new ArrayList<>();
            List<Long> studentIds = studentRepository.findAll().stream().map(Student::getId).collect(Collectors.toList());
            for (int i = 0; i < totalChunks; i++) {
                tasks.add(() -> {
                    seedGradeWorker.seedChunk(studentIds, chunkSize, completed, loggedSteps, System.currentTimeMillis());
                    return null;
                });
            }
            executorService.invokeAll(tasks);

            log.info("Populating with random grades done");
            log.info("Grades added: {}", gradeRepository.count());
            sendNotificationToFrontendService.notifyFrontendAboutSeedingGradesStatus("Seeding Done!");
            recordsReadyToExport = true;
        } catch (InterruptedException ie) {
            String errorMessage = "InterruptedException :: Error seeding grades among students using " +
                    "multithreading strategy, original exception: " + ie.getMessage();
            log.error(errorMessage);
            throw new InterruptedException(errorMessage);
        } finally {
            executorService.shutdown();
        }
    }
}
