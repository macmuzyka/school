package com.school.service

import com.school.controller.FrontendNotificationSenderService
import com.school.model.dto.GradeDTO
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


@Service
@Profile("default", "prod")
class GradeConsumingByKafkaService(
        private val frontendNotificationSenderService: FrontendNotificationSenderService,
        private val gradeService: GradeService
) {
    private val log = LoggerFactory.getLogger(GradeConsumingByKafkaService::class.java)

    @KafkaListener(topics = [
        "math-grade-supplier",
        "history-grade-supplier",
        "english-grade-supplier",
        "art-grade-supplier"
    ],
            groupId = "grades")
    fun consumeGradeFromGradesGroupId(grade: GradeDTO) {
        log.debug("[KAFKA LISTENER] -> received message in [grades] group ID")
        log.info("Incoming object: {}", grade)
        val savedGrade = gradeService.addGrade(grade)
        savedGrade?.let { sendNotificationToFrontend("OK") } ?: sendNotificationToFrontend("ERROR")
    }

    private fun sendNotificationToFrontend(notificationMessage: String) {
        frontendNotificationSenderService.notifyFrontendAboutGradeMessageConsumed(notificationMessage)
    }
}

