package com.school.controller

import com.school.model.dto.FeedbackDTO
import com.school.service.FeedbackService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feedback")
class FeedbackController(
        private val feedbackService: FeedbackService
) {
    @PostMapping("/add")
    fun saveFeedback(@RequestBody feedback: FeedbackDTO): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.propagateFeedback(feedback))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}