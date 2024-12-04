package com.school.service

import com.school.model.dto.FeedbackDTO
import com.school.model.response.MailResponse
import com.school.service.utils.FeedbackResponseProvider.Companion.buildMailResponse
import com.school.service.utils.FeedbackResponseProvider.Companion.prepareFeedbackProviderMailMessage
import com.school.service.utils.FeedbackResponseProvider.Companion.prepareFeedbackProviderMailSubject
import com.school.service.utils.FeedbackResponseProvider.Companion.prepareSupportMailSubject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class FeedbackMailSenderService(
        private val javaMailSender: JavaMailSender,
        @Value("\${support-mail}")
        private val supportMail: String
) {
    private val log = LoggerFactory.getLogger(FeedbackMailSenderService::class.java)

    fun sendMailWithFeedback(feedback: FeedbackDTO): MailResponse {
        val (providerName, providerMail, feedbackContent) = feedback
        val supportInformed = mailToSupportSent(providerName, providerMail, feedbackContent)
        val providerInformed = responseToFeedbackProviderSent(providerName, providerMail)
        return responseFromSendingMails(supportInformed, providerInformed)
    }

    private fun mailToSupportSent(providerName: String, providerMail: String, feedbackContent: String): Boolean {
        try {
            val mail = SimpleMailMessage()
            mail.setTo(supportMail)
            mail.subject = prepareSupportMailSubject(providerName, providerMail)
            mail.text = feedbackContent
            javaMailSender.send(mail)
            log.info("Mail to support sent!")
            return true
        } catch (e: Exception) {
            log.error("Error sending mail to support!")
            log.error(e.message)
            return false
        }
    }

    private fun responseToFeedbackProviderSent(providerName: String, providerMail: String): Boolean {
        try {
            val mail = SimpleMailMessage()
            mail.setTo(providerMail)
            mail.subject = prepareFeedbackProviderMailSubject(providerName)
            mail.text = prepareFeedbackProviderMailMessage()
            javaMailSender.send(mail)
            log.info("Mail to feedback provider sent!")
            return true
        } catch (e: Exception) {
            log.error("Error sending mail to feedback provider!")
            log.error(e.message)
            return false
        }
    }

    private fun responseFromSendingMails(supportInformed: Boolean, providerInformed: Boolean): MailResponse {
        return buildMailResponse(supportInformed, providerInformed)
    }
}