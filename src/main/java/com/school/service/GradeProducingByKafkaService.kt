package com.school.service

import com.school.model.dto.GradeDTO
import com.school.repository.sql.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GradeProducingByKafkaService(
    private val subjectRepository: SubjectRepository,
    private val kafkaTemplate: KafkaTemplate<String, GradeDTO>,
    @Value("\${app.config.grade-topic}")
        private val kafkaGradeSupplierTopic: String

) {
    private val log = LoggerFactory.getLogger(GradeProducingByKafkaService::class.java)
    fun produceGradeAndSendViaKafka(grade: GradeDTO): Boolean {
        try {
            val topicPrefix = resolvePrefixValue(grade.subjectId)
            topicPrefix?.run {
                val fullTopicName = topicPrefix + kafkaGradeSupplierTopic
                log.info("Sending grade $grade via kafka topic $fullTopicName")
                kafkaTemplate.send(fullTopicName, grade)
                return true
            } ?: throw IllegalArgumentException("Could not resolve topic prefix grade should be produced & sent to!")
        } catch (e: Exception) {
            log.error(e.message ?: "Error while sending")
            throw RuntimeException(e)
        }
    }

    private fun resolvePrefixValue(subjectId: Long?): String? {
        subjectId?.let {
            return subjectRepository.findById(subjectId)
                    .takeIf { it.isPresent }
                    ?.get()
                    ?.name?.lowercase()
        } ?: throw IllegalArgumentException("Subject id in incoming grade object was null!")
    }
}