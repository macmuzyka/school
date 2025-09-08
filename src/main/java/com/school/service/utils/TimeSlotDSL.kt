package com.school.service.utils

import com.school.model.entity.classschedule.ClassSchedule
import com.school.model.entity.classschedule.ScheduleEntry
import com.school.model.entity.classschedule.TimeSlot
import java.time.LocalTime

fun ClassSchedule.getBeginningOfTargetSlot(targetSlotIndex: Int): LocalTime = this.scheduleEntries
    .first().timeSlots
    .filter { it.isNotBreak }[targetSlotIndex].startTime

fun LocalTime.isNotLaterThan(other: LocalTime) = this.isBefore(other) ||
        (this.hour == other.hour && this.minute == other.minute && this.second == other.second)

fun ScheduleEntry.canHaveMoreClass(maxClassPerDay: Int) = this.timeSlots.countClasses() < maxClassPerDay
private fun List<TimeSlot>.countClasses() = this.count { it.isNotBreak && it.subject != null }

fun TimeSlot.isNotBreakAndHasClass() = this.isNotBreak && this.subject != null

fun List<TimeSlot>.isConsistentClassChain(): Boolean {
    val nextNonBreakClassSlotIdIncrement = 2
    for (i in 1 until this.size) {
        if (this[i].id != (this[i - 1].id + nextNonBreakClassSlotIdIncrement)) {
            return false
        }
    }

    return true
}

fun List<TimeSlot>.findRandomBeginningTimeSlot(maxSlotIndex: Int) =
    filterIndexed { index, _ -> index < maxSlotIndex }.random()

fun List<TimeSlot>.findNextFreeTimeSlot(latestClassTimeSlot: TimeSlot) = this.first { it.id > latestClassTimeSlot.id }

fun List<TimeSlot>.alreadyHaveFirstClass(): TimeSlot? = this/*.sortedBy { it.id }*/.lastOrNull { it.subject != null }
