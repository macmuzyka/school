package com.school.service.classschedule

import com.school.configuration.ClassScheduleConfig
import com.school.model.entity.Subject
import com.school.service.SubjectService
import org.springframework.stereotype.Component

@Component
class ClassDispenserForScheduleGenerationProvider(
    private val classScheduleConfig: ClassScheduleConfig,
    private val subjectService: SubjectService,
) {
    fun build(schoolClassId: Long): MutableMap<Subject, Int> {
        val subjects = subjectService.getSubjectsBySchoolClassId(schoolClassId)
        return subjects.associateWith { classScheduleConfig.maxSubjectClassPerWeek }.toMutableMap()
    }
}