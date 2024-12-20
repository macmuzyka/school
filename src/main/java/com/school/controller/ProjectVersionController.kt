package com.school.controller

import com.school.service.ProjectVersionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/version")
class ProjectVersionController(
    private val projectVersionService: ProjectVersionService
) {
    @GetMapping("/current")
    fun getCurrentVersion(): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(projectVersionService.getCurrentProjectVersion())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}