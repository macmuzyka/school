package com.school.service

import com.school.model.dto.GradeDTO
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


@Service
@Profile("default", "prod")
class GradeConsumingByKafkaService(
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
        log.info("[KAFKA LISTENER] -> received message in [grades] group ID")
        log.info("[Received object] -> {}", grade)
        gradeService.addGrade(grade).also { log.info("Saved grade: $it") }
    }
}

