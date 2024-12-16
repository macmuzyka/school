package com.school.repository

import com.school.model.entity.PreviousNote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PreviousNoteRepository : JpaRepository<PreviousNote, Long> {
    fun findPreviousNotesBySubject(subject: String): List<PreviousNote>
}