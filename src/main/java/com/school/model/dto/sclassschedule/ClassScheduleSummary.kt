package com.school.model.dto.sclassschedule

import java.time.DayOfWeek

data class ClassScheduleSummary(
    val id: Long,
    val dayClasses: Map<Pair<DayOfWeek, Int>, List<String>>,
    val totalClasses: Int
)