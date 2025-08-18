package com.school.service.classschedule

import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.SchoolClassService
import com.school.service.SubjectService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduleSeederService(
    private val classSeederService: ClassSeederService,
    private val schoolClassService: SchoolClassService,
    private val classScheduleService: ClassScheduleService,
    private val emptyScheduleSchemaBuilderService: EmptyScheduleSchemaBuilderService,
    private val subjectService: SubjectService
) {
    @Transactional
    fun seedScheduleWithClasses(scheduleId: Long): ClassScheduleSummary =
        run {
            var schedule = classScheduleService.getClassSchedule(scheduleId)
            val schoolClass = schoolClassService.getSchoolClass(schedule.schoolClass.id)
            val subjects = subjectService.getSubjectsBySchoolClassId(schoolClass.id)
            schedule = emptyScheduleSchemaBuilderService.generateEmptySchedule(schoolClass)

            val seededSchedule = classSeederService.seedClasses(schedule, subjects)
            val updatedClassSchedule = classScheduleService.updateClassSchedule(seededSchedule)

            return updatedClassSchedule.asQuickView()
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