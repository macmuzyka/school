package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.Subject
import com.school.service.SubjectService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassesLeftToDispenseService(
    private val classScheduleService: ClassScheduleService,
    private val classScheduleConfig: ClassScheduleConfig,
    private val subjectService: SubjectService
) {
    private val log = LoggerFactory.getLogger(ClassesLeftToDispenseService::class.java)

    fun getClassesLeft(scheduleId: Long): MutableMap<Subject, Int> {
        val schedule = classScheduleService.getClassScheduleById(scheduleId)
        val allSubjects = subjectService.getSubjectsBySchoolClassId(schedule.schoolClass.id)
        return allSubjects
            .associateWith { subject ->
                schedule.scheduleEntries
                    .flatMap { it.timeSlots }
                    .filter { it.isNotBreak && it.subject != null }
                    .fold(classScheduleConfig.maxSubjectClassPerWeek) { classesLeftToDispense, timeslot ->
                        classesLeftToDispense - (if (timeslot.subject == subject) 1 else 0)
                    }
            }.toMutableMap()
            .also { log.info("Classes left: $it") }
    }
}