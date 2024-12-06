package com.school.service

import com.school.model.dto.FeedbackDTO
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CentralRegistryService(
        private val kafkaTemplate: KafkaTemplate<String, FeedbackDTO>
) {
    private val log = LoggerFactory.getLogger(CentralRegistryService::class.java)

    fun deliverFeedback(feedback: FeedbackDTO): String {
        return try {
            sendFeedbackViaKafka(feedback)
        } catch (e: Exception) {
            e.message ?: "Error while sending feedback via kafka to central registry"
        }
    }

    private fun sendFeedbackViaKafka(feedback: FeedbackDTO): String {

        kafkaTemplate.send("feedback-supplier", feedback)
        return "Message sent via Kafka"
    }
}