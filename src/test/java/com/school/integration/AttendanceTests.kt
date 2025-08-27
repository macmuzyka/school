package com.school.integration

import com.school.model.AttendanceDTO
import com.school.model.StudentPresenceDTO
import com.school.model.dto.AttendanceResponse
import com.school.repository.StudentRepository
import com.school.service.InputStudentsFromTextFileService
import com.school.service.attendance.AttendanceService
import com.school.service.attendance.PresenceStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.test.*

@SpringBootTest
@ActiveProfiles("prod")
class AttendanceTests(
    @Autowired private val attendanceService: AttendanceService,
    @Autowired private val studentRepository: StudentRepository,
    @Autowired private val inputStudentsFromTextFileService: InputStudentsFromTextFileService
) {
    private lateinit var request: AttendanceDTO
    private lateinit var attendanceResponse: AttendanceResponse


    @BeforeTest
    fun setUp() {
        studentRepository.deleteAll()
        val studentsFile = File("src/test/resources/students_list.txt")
        val inputStream: InputStream = FileInputStream(studentsFile)
        val mpf: MultipartFile = MockMultipartFile("file", studentsFile.name, "text/plain", inputStream)
        val studentIds = inputStudentsFromTextFileService.addStudents(mpf, false).map { it.id }.subList(0, 12)
        request = AttendanceDTO(
            1L,
            listOf(
                StudentPresenceDTO(studentIds[0], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[1], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[2], PresenceStatus.ABSENT, "no info yet about absence"),
                StudentPresenceDTO(studentIds[3], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[4], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[5], PresenceStatus.LATE, "10 min late because of high traffic"),
                StudentPresenceDTO(studentIds[6], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[7], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[8], PresenceStatus.LEFT_EARLY, "doctor appointment"),
                StudentPresenceDTO(studentIds[9], PresenceStatus.PRESENT, ""),
                StudentPresenceDTO(studentIds[10], PresenceStatus.ABSENT, "official sick leave provided"),
                StudentPresenceDTO(studentIds[11], PresenceStatus.PRESENT, ""),
            )
        )
    }

    @AfterTest
    fun cleanup() {
        attendanceService.removeAttendance(attendanceResponse.id)
        studentRepository.deleteAll()
    }

    @Test
    fun shouldProperlySaveAttendance() {
        attendanceResponse = attendanceService.saveAttendance(request)
        assertNotEquals(0, attendanceResponse.id)
    }

    @Test
    fun shouldProperlyGroupStudents() {
        attendanceResponse = attendanceService.saveAttendance(request)
//        assertEquals(8, attendanceResponse.presenceList[PresenceStatus.PRESENT]?.size)
        assertEquals(2, attendanceResponse.presenceList[PresenceStatus.ABSENT]?.size)
        assertEquals(1, attendanceResponse.presenceList[PresenceStatus.LEFT_EARLY]?.size)
        assertEquals(1, attendanceResponse.presenceList[PresenceStatus.LATE]?.size)
    }
}