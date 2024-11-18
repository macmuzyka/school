package com.school

import com.school.repository.GradeRepository
import com.school.service.InputStudentsFromTextFileService
import com.school.service.ProgressRecordExportService
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream


@SpringBootTest
@ActiveProfiles(profiles = ["prod"])
class PerformanceTests(@Autowired
                       private val inputStudentsFromTextFileService: InputStudentsFromTextFileService,
                       @Autowired
                       private val progressRecordExportService: ProgressRecordExportService,
                       @Autowired
                       private val gradeRepository: GradeRepository
) {


    private val numberOfTests = 10
    private val log = LoggerFactory.getLogger(PerformanceTests::class.java)
    private val file = File("src/test/resources/students_list.txt")


    private val inputStream: FileInputStream = FileInputStream(file)
    private val multipartFile: MultipartFile = MockMultipartFile("file", file.getName(), "text/plain", inputStream)

    @Test
    fun runPerformanceTests() {
        log.info("Running performance tests $numberOfTests times")
        repeat(numberOfTests) {
            inputStudentsFromTextFileService.addStudents(multipartFile)
            progressRecordExportService.scheduledExportTask()
            gradeRepository.deleteAll()
        }
    }
}