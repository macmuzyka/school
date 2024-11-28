package com.school.controller

import com.school.service.DuplicatedStudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/duplicated")
class DuplicatedStudentController(
        private val duplicatedStudentService: DuplicatedStudentService
) {
    @GetMapping("/all")
    fun getDuplicatedStudents(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(duplicatedStudentService.getAllDuplicatedStudents())
    }

    @DeleteMapping("/delete/{studentId}")
    fun removeDuplicatedStudent(@PathVariable studentId: Long): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(duplicatedStudentService.deleteStudent(studentId))
    }

    @DeleteMapping("/delete-all")
    fun deleteDuplicatedStudents(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(duplicatedStudentService.deleteAll())
    }

    @DeleteMapping("/delete-selected")
    fun removeSelectedDuplicatedStudents(@RequestBody idsToRemove: List<Long>): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(duplicatedStudentService.deleteDuplicatedStudentsWithIds(idsToRemove))
    }
}