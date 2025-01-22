package com.school.service;

import com.school.model.response.BackupResponse;
import com.school.model.statistics.ProgressRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class    SendNotificationToFrontendService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(SendNotificationToFrontendService.class);

    public SendNotificationToFrontendService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyFrontendAboutGradeConsumptionStatus(String message) {
        logMessageSentViaWebsocket("grade adding success", message);
        messagingTemplate.convertAndSend("/topic/grade-consume-status", message);
    }

    public void notifyFrontendAboutUploadingFileStatus(String message) {
        logMessageSentViaWebsocket("file upload status", message);
        messagingTemplate.convertAndSend("/topic/file-upload-status", message);
    }

    public void notifyFrontendAboutStudentInsertErrorDetected(String message) {
        logMessageSentViaWebsocket("student insert error detected", message);
        messagingTemplate.convertAndSend("/topic/student-duplicate-detected", message);
    }

    public void notifyFrontendAboutStudentDuplicateDetected(String message) {
        logMessageSentViaWebsocket("student duplicate detected", message);
        messagingTemplate.convertAndSend("/topic/student-insert-error-detected", message);
    }

    public void notifyFrontendAboutSeedingGradesStatus(String message) {
        logMessageSentViaWebsocket("seeding grades", message);
        messagingTemplate.convertAndSend("/topic/seeding-grade", message);
    }

    public void notifyFrontendAboutStudentDuplicateRemoval(String message) {
        logMessageSentViaWebsocket("student duplicate removal", message);
        messagingTemplate.convertAndSend("/topic/duplicate-removal", message);
    }

    public void notifyFrontendAboutStudentInsertErrorCorrection(String message) {
        logMessageSentViaWebsocket("student insert error correction", message);
        messagingTemplate.convertAndSend("/topic/insert-error-correction", message);
    }

    public void notifyFrontendAboutStudentInsertErrorRemoval(String message) {
        logMessageSentViaWebsocket("student insert error correction", message);
        messagingTemplate.convertAndSend("/topic/insert-error-removal", message);
    }

    public void notifyFrontendAboutStudentUpdateStatus(String message) {
        logMessageSentViaWebsocket("student update status", message);
        messagingTemplate.convertAndSend("/topic/student-update-status", message);
    }

    public void notifyFrontendAboutStudentRemovalStatus(String message) {
        logMessageSentViaWebsocket("student removal status", message);
        messagingTemplate.convertAndSend("/topic/student-removal-status", message);
    }

    public void notifyFrontendAboutFeedbackConsumeStatus(String message) {
        logMessageSentViaWebsocket("feedback consume status", message);
        messagingTemplate.convertAndSend("/topic/feedback-consume-status", message);
    }

    public void notifyFrontendAboutAddedStudent(String message) {
        logMessageSentViaWebsocket("new student added", message);
        messagingTemplate.convertAndSend("/topic/student-added", message);
    }

    public void notifyFrontendAboutSeedingProgress(ProgressRecord currentProgressRecord) {
        String message = "Seeding " + currentProgressRecord.getPercentageProgress() + "% took " + currentProgressRecord.getDuration() + "s";
        logMessageSentViaWebsocket("seeding progress", message);
        messagingTemplate.convertAndSend("/topic/seeding-progress", message);
    }

    public void notifyFrontendAboutDatabaseRestoreResult(BackupResponse backupResponse) {
        String message;
        if (backupResponse.code() == 0) {
            message = "Database restored successfully!";
        } else {
            message = "There was some errors while restoring database from backup file: \n" + backupResponse.message();
        }
        logMessageSentViaWebsocket("database restore", message);
        messagingTemplate.convertAndSend("/topic/database-restore", message);
    }

    private void logMessageSentViaWebsocket(String about, String message) {
        log.info("Message to be send about {}: {}", about, message);
    }
}
