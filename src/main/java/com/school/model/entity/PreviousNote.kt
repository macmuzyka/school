package com.school.model.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "previous_note")
@EntityListeners(AuditingEntityListener::class)
data class PreviousNote(
        val content: String = "",
        @Column(length = 50)
        val subject: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = 0L

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
}
