package com.school

import com.school.configuration.ApplicationConfig
import com.school.configuration.ClassScheduleConfig
import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.classschedule.ClassSchedule
import com.school.repository.classschedule.ClassScheduleRepository
import com.school.service.classschedule.ClassScheduleService
import com.school.service.classschedule.ScheduleSeederService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalTime
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("prod")
class ScheduleSeederTests(
    @Autowired val scheduleSeederService: ScheduleSeederService,
    @Autowired val classScheduleService: ClassScheduleService,
    @Autowired val classScheduleRepository: ClassScheduleRepository,
    @Autowired val classScheduleConfig: ClassScheduleConfig,
    @Autowired val applicationConfig: ApplicationConfig
) {
    private lateinit var emptySchedule: ClassSchedule
    private lateinit var seededSchedule: ClassScheduleSummary

    @BeforeEach
    fun findAnyScheduleId() {
        emptySchedule = classScheduleRepository.findAll().first()
        seededSchedule = scheduleSeederService.seedScheduleWithClasses(emptySchedule.id)
    }

    @Test
    fun shouldSeedNumberOfClassesFromConfiguration() {
        val expectedSeededLessonCalculatedFromConfig =
            applicationConfig.availableSubjects.size * classScheduleConfig.maxLessonPerSubject
        assertEquals(expectedSeededLessonCalculatedFromConfig, seededSchedule.totalClasses)
    }

    @Test
    @Disabled
    fun shouldSeedMaximumAtThirdClassTimeframe() {
        val generatedSchedule = classScheduleService.getClassSchedule(seededSchedule.id)
        val timeSlotsBeginnings = generatedSchedule.scheduleEntries.first().timeSlots
        timeSlotsBeginnings.sortBy { it.startTime }
        val latestStartOfClass = timeSlotsBeginnings[2].startTime
        assertFalse(scheduleHasNoClassLaterThanThirdTimeframe(generatedSchedule, latestStartOfClass))
    }

    private fun scheduleHasNoClassLaterThanThirdTimeframe(
        generatedSchedule: ClassSchedule,
        latestStartOfClass: LocalTime
    ) = generatedSchedule.scheduleEntries
        .flatMap { entry -> entry.timeSlots }
        .any { it.startTime < latestStartOfClass }
}