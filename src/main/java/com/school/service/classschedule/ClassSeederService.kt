package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.Subject
import com.school.model.entity.classschedule.ClassSchedule
import com.school.model.entity.classschedule.ScheduleEntry
import com.school.service.utils.TimeSlotUtils.Companion.countAvailableLesson
import com.school.service.utils.TimeSlotUtils.Companion.doesNotExceedMaxClassStartTime
import com.school.service.utils.TimeSlotUtils.Companion.getBeginningOfTargetSlot
import com.school.service.utils.TimeSlotUtils.Companion.isNotBreakAndIsFreeToHaveClass
import com.school.service.utils.oneLess
import org.springframework.stereotype.Service

@Service
class ClassSeederService(
    private val timeSlotManagingService: TimeSlotManagingService,
    private val classScheduleConfig: ClassScheduleConfig,
) {
    fun seedClasses(schedule: ClassSchedule, subjects: List<Subject>): ClassSchedule {
        val classDispenser = prepareClassDispenser(subjects)
        while (classDispenser.isNotEmpty()) {
            val currentSubject = classDispenser.keys.random()
            classDispenser oneLess currentSubject

            val latestClassStart = schedule.getBeginningOfTargetSlot(classScheduleConfig.latestClassScheduleStart)
            val scheduleEntryToSeed = schedule.scheduleEntries
                .filter { entry -> entry.timeSlots.countAvailableLesson() <= classScheduleConfig.maxClassPerDay }
                .random()
            val beginningTimeSlotId = scheduleEntryToSeed.timeSlots
                .firstOrNull { it.isNotBreakAndIsFreeToHaveClass() && it.doesNotExceedMaxClassStartTime(latestClassStart) }
            val timeSlotId = beginningTimeSlotId?.id ?: findLaterTimeSlotId(scheduleEntryToSeed)
            timeSlotManagingService.assignSubjectToTimeSlot(currentSubject.id, timeSlotId)
        }
        return schedule
    }

    private fun prepareClassDispenser(subjects: List<Subject>) =
        HashMap(subjects.associateWith { classScheduleConfig.maxSubjectClassPerWeek })

    private fun findLaterTimeSlotId(schedule: ScheduleEntry) =
        schedule.timeSlots.filter { it.isNotBreakAndIsFreeToHaveClass() }.sortedBy { it.id }.first().id
}