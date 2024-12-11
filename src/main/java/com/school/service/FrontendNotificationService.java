package com.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendNotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(FrontendNotificationService.class);

    public FrontendNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyFrontendAboutGradeMessageConsumed(String message) {
        logMessageSentViaWebsocket("grade added", message);
        messagingTemplate.convertAndSend("/topic/message-consume-status", message);
    }

    public void notifyFrontendAboutStudentInsertError(String message) {
        logMessageSentViaWebsocket("student duplicate", message);
        messagingTemplate.convertAndSend("/topic/student-duplicate", message);
    }

    public void notifyFrontendAboutStudentDuplicate(String message) {
        logMessageSentViaWebsocket("student insert error", message);
        messagingTemplate.convertAndSend("/topic/student-insert-error", message);
    }

    private void logMessageSentViaWebsocket(String about, String message) {
        log.info("Message to be send about {}: {}", about, message);
    }
}
