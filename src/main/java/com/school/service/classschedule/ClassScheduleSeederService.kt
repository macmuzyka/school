package com.school.service.classschedule

import com.school.configuration.validate.ConfigurationValidator
import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.utils.isNotBreakAndHasClass
import com.school.service.utils.asQuickView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClassScheduleSeederService(
    private val classSeederService: ClassSeederService,
    private val classScheduleService: ClassScheduleService,
    private val configurationValidator: ConfigurationValidator
) {
    @Transactional
    fun seedScheduleWithClasses(classId: Long): ResponseWrapper {
        return classId.getSchedule()
            .purge()
            .seedClasses()
            .asResponseWrapper()
    }

    fun getClassScheduleSummary(classId: Long): ClassScheduleSummary {
        return classId.getSchedule()
            .asQuickView()
    }

    private fun Long.getSchedule() = classScheduleService.getClassScheduleByClassId(this)

    private fun ClassSchedule.purge(): ClassSchedule {
        classScheduleService.purgeClassSchedule(this)
        return this
    }

    private fun ClassSchedule.seedClasses(): ClassSchedule {
        val seededSchedule = classSeederService.seedClasses(this)
        return classScheduleService.updateClassSchedule(seededSchedule)
    }

    private fun ClassSchedule.asResponseWrapper() =
        ResponseWrapper(resolveScheduleGenerationCorrectness())

    private fun ClassSchedule.resolveScheduleGenerationCorrectness() =
        this.scheduleEntries
            .flatMap { it.timeSlots }
            .count { it.isNotBreakAndHasClass() } == configurationValidator.expectedNumberOfGeneratedClasses()

    data class ResponseWrapper(val created: Boolean)
}