package com.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendNotificationSenderService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(FrontendNotificationSenderService.class);

    public FrontendNotificationSenderService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyFrontendAboutGradeMessageConsumed(String message) {
        log.info("Message to be send: {}", message);
        messagingTemplate.convertAndSend("/topic/message-consume-status", message);
    }
}
