package com.school.repository

import com.school.model.entity.StudentDuplicateError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentDuplicateErrorRepository: JpaRepository<StudentDuplicateError, Long>