package com.school

import com.school.condition.shutdown.KafkaDockerStopExtension
import com.school.configuration.ApplicationConfig
import com.school.repository.SubjectRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("prod")
@ExtendWith(KafkaDockerStopExtension::class)
class SubjectTests(
    @Autowired val applicationConfig: ApplicationConfig,
    @Autowired val subjectRepository: SubjectRepository
) {
    @Test
    fun numberOfCreatedSubjectsUponWarmupShouldBeEqualToConfiguration() {
        assertEquals(applicationConfig.availableSubjects.size, subjectRepository.findBySchoolClassId(1L).size)
    }
}