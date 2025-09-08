package com.school.service.utils

import com.school.model.entity.classschedule.ScheduleEntry

fun ScheduleEntry.stillHasFreeLastTimeSlot(): Boolean {
    return this.timeSlots.sortedBy { it.id }
        .last { it.isNotBreak }
        .takeIf { it.subject == null }
        ?.let { true } ?: false
}
