package com.school.repository.attendance

import com.school.model.entity.attendance.StudentPresence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentPresenceRepository : JpaRepository<StudentPresence, Long>