package com.school.service.utils

import com.school.model.entity.classschedule.ClassSchedule
import com.school.model.entity.classschedule.TimeSlot
import java.time.LocalTime

class TimeSlotUtils {
    companion object {
        fun TimeSlot.isNotBreakAndIsFreeToHaveClass() = this.isNotBreak && this.subject == null
        fun TimeSlot.isNotBreakAndHasClass() = this.isNotBreak && this.subject != null

        @JvmStatic
        fun isNotBreak(timeSlot: TimeSlot) = timeSlot.isNotBreak

        fun LocalTime.isNotLaterThan(other: LocalTime) = this.isBefore(other) ||
                (this.hour == other.hour && this.minute == other.minute && this.second == other.second)

        fun TimeSlot.doesNotExceedMaxClassStartTime(maxClassStartTime: LocalTime) =
            this.startTime.isNotLaterThan(maxClassStartTime)

        fun List<TimeSlot>.isChain(): Boolean {
            for (i in 1 until this.size) {
                if (this[i].id != (this[i - 1].id + 2)) {
                    return false
                }
            }

            return true
        }

        fun ClassSchedule.getBeginningOfTargetSlot(targetSlotIndex: Int): LocalTime = this.scheduleEntries
            .first().timeSlots
            .filter { it.isNotBreak }[targetSlotIndex].startTime

        fun List<TimeSlot>.countAvailableLesson() = this.count { it.isNotBreakAndIsFreeToHaveClass() }

    }
}
