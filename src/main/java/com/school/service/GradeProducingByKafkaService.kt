package com.school.service

import com.school.model.dto.GradeDTO
import com.school.repository.SubjectRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
@Profile("default", "prod")
class GradeProducingByKafkaService(
        private val subjectRepository: SubjectRepository,
        private val kafkaTemplate: KafkaTemplate<String, GradeDTO>,
        @Value("\${app.config.grade-topic}")
        private val kafkaTopic: String

) {
    fun produceGradeAndSendViaKafka(grade: GradeDTO): String {
        try {
            val topicPrefix = resolvePrefixValue(grade.subjectId)
            topicPrefix?.run {
                val fullTopicName = topicPrefix + kafkaTopic
                kafkaTemplate.send(fullTopicName, grade)
            } ?: throw IllegalArgumentException("Could not resolve topic prefix grade should be produced to!")
            return "OK"
        } catch (e: Exception) {
            return e.message ?: "Error while sending"
        }
    }

    private fun resolvePrefixValue(subjectId: Long?): String? {
        subjectId?.let {
            return subjectRepository.findById(subjectId).takeIf { it.isPresent }?.get()?.name?.lowercase()
        } ?: throw IllegalArgumentException("Subject id in incoming grade object was null!")
    }
}