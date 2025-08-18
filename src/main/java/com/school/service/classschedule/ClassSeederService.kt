package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.Subject
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.utils.TimeSlotUtils.Companion.isNotBreakAndIsFreeToHaveClass
import com.school.service.utils.oneLess
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassSeederService(
    private val timeSlotManagingService: TimeSlotManagingService,
    private val classScheduleConfig: ClassScheduleConfig,
) {
    private val log = LoggerFactory.getLogger(ClassSeederService::class.java)
    fun seedClasses(schedule: ClassSchedule, subjects: List<Subject>): ClassSchedule {
        val classDispenser = prepareClassDispenser(subjects)
        while (classDispenser.isNotEmpty()) {
            val currentRandomSubject = classDispenser.keys.random()
            classDispenser oneLess currentRandomSubject
            val randomEmptyTimeSlotId = schedule.scheduleEntries
                .flatMap { it.timeSlots }
                .filter { it.isNotBreakAndIsFreeToHaveClass() }
                .random().id
            timeSlotManagingService.assignSubjectToTimeSlot(currentRandomSubject.id, randomEmptyTimeSlotId)
        }
        return schedule
    }

    private fun prepareClassDispenser(subjects: List<Subject>): HashMap<Subject, Int> {
        return HashMap(subjects.associateWith { classScheduleConfig.maxLessonPerSubject }).also {
            log.debug(
                "Prepared class dispenser: {}",
                it
            )
        }
    }
}