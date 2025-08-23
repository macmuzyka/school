package com.school.service.utils

import com.school.model.entity.classschedule.ClassSchedule
import com.school.model.entity.classschedule.ScheduleEntry
import com.school.model.entity.classschedule.TimeSlot
import java.time.LocalTime

class TimeSlotUtils {
    companion object {
        fun ClassSchedule.getBeginningOfTargetSlot(targetSlotIndex: Int): LocalTime = this.scheduleEntries
            .first().timeSlots
            .filter { it.isNotBreak }[targetSlotIndex].startTime

        fun TimeSlot.isNotBreakAndIsFreeToHaveClass() = this.isNotBreak && this.subject == null

        fun TimeSlot.doesNotExceedMaxClassStartTime(maxClassStartTime: LocalTime) =
            this.startTime.isNotLaterThan(maxClassStartTime)

        fun LocalTime.isNotLaterThan(other: LocalTime) = this.isBefore(other) ||
                (this.hour == other.hour && this.minute == other.minute && this.second == other.second)

        fun ScheduleEntry.canHaveMoreClass(maxClassPerDay: Int) = this.timeSlots.countClasses() < maxClassPerDay
        private fun List<TimeSlot>.countClasses() = this.count { it.isNotBreak && it.subject != null }

        fun TimeSlot.isNotBreakAndHasClass() = this.isNotBreak && this.subject != null

        @JvmStatic
        fun isNotBreak(timeSlot: TimeSlot) = timeSlot.isNotBreak

        fun List<TimeSlot>.isConsistentClassChain(): Boolean {
            val nextNonBreakClassSlotIdIncrement = 2
            for (i in 1 until this.size) {
                if (this[i].id != (this[i - 1].id + nextNonBreakClassSlotIdIncrement)) {
                    return false
                }
            }

            return true
        }
    }
}
