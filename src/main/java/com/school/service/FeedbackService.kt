package com.school.service

import com.school.model.dto.FeedbackDTO
import com.school.model.response.MailResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FeedbackService(
        private val feedbackMaileSenderService: FeedbackMailSenderService
) {
    fun propagateFeedback(feedback: FeedbackDTO): MailResponse {
        return feedbackMaileSenderService.sendMailWithFeedback(feedback)
    }
}