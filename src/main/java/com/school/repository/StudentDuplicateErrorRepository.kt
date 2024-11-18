package com.school.repository

import com.schoolmodel.model.entity.StudentDuplicateError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentDuplicateErrorRepository: JpaRepository<StudentDuplicateError, Long>