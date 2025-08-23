package com.school.integration

import com.school.configuration.ApplicationConfig
import com.school.configuration.ClassScheduleConfig
import com.school.model.dto.sclassschedule.ClassScheduleSummary
import com.school.model.entity.SchoolClass
import com.school.model.entity.classschedule.ClassSchedule
import com.school.service.SchoolClassService
import com.school.service.classschedule.ClassScheduleService
import com.school.service.classschedule.ScheduleSeederService
import com.school.service.utils.TimeSlotUtils.Companion.getBeginningOfTargetSlot
import com.school.service.utils.TimeSlotUtils.Companion.isConsistentClassChain
import com.school.service.utils.TimeSlotUtils.Companion.isNotBreakAndHasClass
import com.school.service.utils.TimeSlotUtils.Companion.isNotLaterThan
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("prod")
class ScheduleSeederTests(
    @Autowired val scheduleSeederService: ScheduleSeederService,
    @Autowired val classScheduleService: ClassScheduleService,
    @Autowired val schoolClassService: SchoolClassService,
    @Autowired val classScheduleConfig: ClassScheduleConfig,
    @Autowired val applicationConfig: ApplicationConfig
) {
    private lateinit var emptySchedule: ClassSchedule
    private lateinit var seededSchedule: ClassScheduleSummary
    private lateinit var testClass: SchoolClass

    @BeforeEach
    fun prepareTestObjects() {
        testClass = schoolClassService.createNewClass()
        emptySchedule = testClass.classSchedule
        seededSchedule = scheduleSeederService.seedScheduleWithClasses(emptySchedule.id)
    }

    @AfterEach
    fun tearDownTestObjects() {
        schoolClassService.removeClass(testClass)
        classScheduleService.removeClassSchedule(emptySchedule)
    }

    @Test
    @Transactional
    fun shouldSeedNumberOfClassesFromConfiguration() {
        val expectedSeededLessonCalculatedFromConfig =
            applicationConfig.availableSubjects.size * classScheduleConfig.maxSubjectClassPerWeek
        assertEquals(expectedSeededLessonCalculatedFromConfig, seededSchedule.totalClasses)
    }

    @Test
    @Transactional
    fun shouldSeedMaximumAtConfigTimeslot() {
        val generatedSchedule = classScheduleService.getClassSchedule(seededSchedule.id)
        println(generatedSchedule.toString())
        val maxClassStartTimeNotExceeded = testEntriesStarts(generatedSchedule)
        assertTrue { maxClassStartTimeNotExceeded }
    }

    private fun testEntriesStarts(classSchedule: ClassSchedule): Boolean {
        val maxClassStartTime =
            classSchedule.getBeginningOfTargetSlot(classScheduleConfig.latestClassScheduleStartAsIndex)
        return classSchedule.scheduleEntries
            .map { entry ->
                entry.timeSlots
                    .filter { it.isNotBreakAndHasClass() }
                    .sortedBy { it.startTime }
                    .firstOrNull().also { println(it?.startTime) }
            }.all { entryFirstClass -> entryFirstClass?.startTime?.isNotLaterThan(maxClassStartTime) ?: true }
    }

    @Test
    @Transactional
    fun generatedScheduleIsChainOfClasses() {
        val generatedSchedule = classScheduleService.getClassSchedule(seededSchedule.id)
        val shouldBeChain = generatedSchedule.scheduleEntries
            .map { entry ->
                entry.timeSlots
                    .filter { timeslot -> timeslot.isNotBreakAndHasClass() }
            }
        for (entry in shouldBeChain) {
            for (timeslot in entry) {
                println(timeslot)
            }
        }

        assertTrue {
            generatedSchedule.scheduleEntries.all { entry ->
                entry.timeSlots.filter { it.isNotBreakAndHasClass() }.isConsistentClassChain()
            }
        }
    }
}