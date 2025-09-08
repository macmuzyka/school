package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.utils.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

//TODO: find a way to get rid of one repeating call of sortedBy{id.id} method in .stillHasFreeLastTimeSlot method
// so its called only once per seedClasses method, now its also used to initialize entryClassTimeSlots value
@Service
class ClassSeederService(
    private val timeSlotAssigningService: TimeSlotAssigningService,
    private val classDispenserForScheduleGenerationProvider: ClassDispenserForScheduleGenerationProvider,
    private val classScheduleConfig: ClassScheduleConfig,
) {
    private val log = LoggerFactory.getLogger(ClassSeederService::class.java)
    fun seedClasses(schedule: ClassSchedule): ClassSchedule {
        try {
            val classDispenser = classDispenserForScheduleGenerationProvider.build(schedule.schoolClass.id)
            while (classDispenser.isNotEmpty()) {
                val currentSubject = classDispenser.keys.random()
                classDispenser oneLess currentSubject

                val scheduleEntryToSeed = schedule.scheduleEntries
                    .filter { entry -> entry.canHaveMoreClass(classScheduleConfig.maxClassPerDay) && entry.stillHasFreeLastTimeSlot() }
                    .random()

                val entryClassTimeSlots = scheduleEntryToSeed.timeSlots
                    .filter { it.isNotBreak }
                    .sortedBy { it.id }
                    .toList()

                val timeSlotToSeed =
                    entryClassTimeSlots.alreadyHaveFirstClass()
                        ?.let { entryClassTimeSlots.findNextFreeTimeSlot(it) }
                        ?: entryClassTimeSlots.findRandomBeginningTimeSlot(classScheduleConfig.latestClassScheduleStart)

                timeSlotAssigningService.assignSubjectToTimeSlot(currentSubject.id, timeSlotToSeed.id)
            }
            return schedule
        } catch (e: Exception) {
            log.error("Exception while seeding classes: $e")
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }
}