package com.school.service.utils

import com.school.model.entity.classschedule.TimeSlot

class TimeSlotUtils {
    companion object {
        fun TimeSlot.isNotBreakAndIsFreeToHaveClass() = this.isNotBreak && this.subject == null
        @JvmStatic
        fun isNotBreak(timeSlot: TimeSlot) = timeSlot.isNotBreak
    }
}
