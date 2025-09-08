package com.school.service.utils

import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.classschedule.ClassSchedule

fun ClassSchedule.asQuickView(): ClassScheduleSummary {
    return ClassScheduleSummary(
        this.id,
        this.scheduleEntries.groupBy(
            keySelector = { entry ->
                Pair(
                    entry.dayOfWeek,
                    entry.timeSlots.count { ts -> ts.isNotBreak && (ts.subject != null && ts.subject.name != "Empty") }
                )
            },
            valueTransform = { entry ->
                entry.timeSlots
                    .filter { it.isNotBreak }
                    .map { it.subject?.name ?: "Empty" }
            }
        ).mapValues { (_, lists) -> lists.flatten() }
            .toMap(HashMap()),
        this.scheduleEntries
            .flatMap { it.timeSlots }
            .count { ts -> ts.isNotBreak && (ts.subject != null && ts.subject.name != "Empty") }
    )
}