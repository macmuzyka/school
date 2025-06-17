package com.school.controller

import com.school.service.SeedGradesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/seed")
class SeedGradesController(
    private val seedGradesService: SeedGradesService
) {
    @PostMapping("/grades")
    fun seedGrades(@RequestParam(defaultValue = "false") optimized: Boolean): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(seedGradesService.seedStudentsWithRandomizedGrades(optimized))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.OK).body(e.message)
        }
    }
}