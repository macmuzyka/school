package com.school.repository.attendance

import com.school.model.entity.attendance.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AttendanceRepository : JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE a.timeSlot.id = :timeSlotId")
    fun findByTimeSlotId(@Param("timeSlotId") timeSlotId: Long): Optional<Attendance>
}