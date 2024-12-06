package com.school.service

import com.school.model.dto.FeedbackDTO
import com.school.model.response.FeedbackPropagationResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FeedbackService(
        private val feedbackMaileSenderService: FeedbackMailSenderService,
        private val centralRegistryService: CentralRegistryService
) {
    private val log = LoggerFactory.getLogger(FeedbackService::class.java)
    fun propagateFeedback(feedback: FeedbackDTO): FeedbackPropagationResponse {
        val mailResponse = feedbackMaileSenderService.sendMailWithFeedback(feedback)
        val centralRegistryResponse = centralRegistryService.deliverFeedback(feedback)
        return FeedbackPropagationResponse(mailResponse, centralRegistryResponse).also { log.info(it.toString()) }
    }
}