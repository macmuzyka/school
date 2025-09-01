package com.school.service.classschedule

import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.SchoolClassService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduleSeederService(
    private val classSeederService: ClassSeederService,
    private val schoolClassService: SchoolClassService,
    private val classScheduleService: ClassScheduleService,
    private val emptyScheduleSchemaBuilderService: EmptyScheduleSchemaBuilderService,
) {
    @Transactional
    fun seedScheduleWithClasses(scheduleId: Long): ClassScheduleSummary {
        return scheduleId.getSchedule()
            .prepareEmptySchedule()
            .seedClasses()
            .asQuickView()
    }

    private fun Long.getSchedule() = classScheduleService.getClassScheduleById(this)
    private fun ClassSchedule.prepareEmptySchedule(): ClassSchedule {
        val schoolClass = schoolClassService.getSchoolClass(this.schoolClass.id)
        classScheduleService.removeClassSchedule(this)

        return emptyScheduleSchemaBuilderService.generateEmptySchedule(schoolClass)
    }

    private fun ClassSchedule.seedClasses(): ClassSchedule {
        val seededSchedule = classSeederService.seedClasses(this)
        return classScheduleService.updateClassSchedule(seededSchedule)
    }

    private fun ClassSchedule.asQuickView(): ClassScheduleSummary {
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
}