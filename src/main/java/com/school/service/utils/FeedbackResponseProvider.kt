package com.school.service.utils

import com.school.model.response.MailResponse

class FeedbackResponseProvider {
    companion object {
        fun prepareSupportMailSubject(providerName: String, at: String): String {
            return "Received Feedback about school project from $providerName @$at"
        }

        fun prepareFeedbackProviderMailSubject(from: String): String {
            return "Thank you for submitting you feedback $from \uD83D\uDE0E"
        }

        fun prepareFeedbackProviderMailMessage(): String {
            return "This application is under constant development process and we are always looking for a ways to improve it!\n" +
                    "Thank you again for providing feedback, we are looking into it\n." +
                    "Have a nice day ☺\uFE0F\n\n" +
                    "[This is auto generated message, do not reply]"
        }

        fun buildMailResponse(supportInformed: Boolean, providerInformed: Boolean): MailResponse {
            return prepareResponse(supportInformed, providerInformed)
        }

        private fun prepareResponse(supportInformed: Boolean, feedbackProviderInformed: Boolean): MailResponse {
            val responseDescription = when {
                supportInformed && feedbackProviderInformed -> "Both support & feedback provider informed via mail"
                supportInformed -> "Only support informed via mail"
                else -> "Only feedback provider informed via mail"
            }
            return MailResponse(supportInformed, feedbackProviderInformed, responseDescription)
        }
    }
}