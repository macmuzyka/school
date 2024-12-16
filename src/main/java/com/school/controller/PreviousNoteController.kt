package com.school.controller

import com.school.model.dto.PreviousNoteDTO
import com.school.service.PreviousNoteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/previous-note")
class PreviousNoteController(private val previousNoteService: PreviousNoteService) {

    @GetMapping
    fun getSubjectNotes(@RequestParam(name = "subjectId", required = true) subject: Long?): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(previousNoteService.getSubjectNotes(subject))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @PostMapping
    fun saveSubjectNote(@RequestBody note: PreviousNoteDTO): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(previousNoteService.saveSubjectNote(note))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}