package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.*
import com.school.service.utils.oneLess
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassSeederService(
    private val timeSlotManagingService: TimeSlotManagingService,
    private val classDispenserForScheduleGenerationProvider: ClassDispenserForScheduleGenerationProvider,
    private val classScheduleConfig: ClassScheduleConfig,
) {
    private val log = LoggerFactory.getLogger(ClassSeederService::class.java)
    fun seedClasses(schedule: ClassSchedule): ClassSchedule {
        val classDispenser = classDispenserForScheduleGenerationProvider.build(schedule.schoolClass.id)
        while (classDispenser.isNotEmpty()) {
            val currentSubject = classDispenser.keys.random()
            classDispenser oneLess currentSubject

            val latestClassStart = schedule.getBeginningOfTargetSlot(classScheduleConfig.latestClassScheduleStart)
            val scheduleEntryToSeed = schedule.scheduleEntries
                .filter { entry -> entry.canHaveMoreClass(classScheduleConfig.maxClassPerDay) }
                .random()
            val firstEverTimeSlotForScheduleEntry = scheduleEntryToSeed.timeSlots
                .firstOrNull { it.isNotBreakAndIsFreeToHaveClass() && it.doesNotExceedMaxClassStartTime(latestClassStart) }
            val timeSlotId =
                firstEverTimeSlotForScheduleEntry?.id ?: findNextTimeSlotForScheduleEntry(scheduleEntryToSeed)
            timeSlotManagingService.assignSubjectToTimeSlot(currentSubject.id, timeSlotId)
        }
        return schedule
    }
}