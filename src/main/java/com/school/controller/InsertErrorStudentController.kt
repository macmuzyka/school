package com.school.controller

import com.school.service.InsertErrorStudentService
import com.schoolmodel.model.dto.StudentDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/insert-error")
class InsertErrorStudentController(
        private val insertErrorStudentService: InsertErrorStudentService
) {
    @PostMapping("/correct")
    fun correctStudent(@RequestBody updatedStudent: StudentDTO): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(insertErrorStudentService.correctStudent(updatedStudent))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @GetMapping("/all")
    fun allInsertErrors(): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(insertErrorStudentService.getInputErrorStudents())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @GetMapping("/{studentId}")
    fun insertError(@PathVariable studentId: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(insertErrorStudentService.getInputErrorStudent(studentId))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @DeleteMapping("/delete/{studentId}")
    fun deleteStudent(@PathVariable studentId: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(insertErrorStudentService.delete(studentId))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}