package com.school;

import com.school.configuration.ApplicationConfig;
import com.school.model.dto.StudentDTO;
import com.school.repository.SchoolClassRepository;
import com.school.repository.StudentRepository;
import com.school.model.entity.Student;
import com.school.service.SendNotificationToFrontendService;
import com.school.service.StudentService;
import com.school.service.fileproducers.StudentsListFileProviderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("prod")
@ExtendWith(MockitoExtension.class)
class StudentTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private StudentsListFileProviderService studentsListFileProviderService;

    @Mock
    private SendNotificationToFrontendService sendNotificationToFrontendService;

    @Mock
    private ApplicationConfig applicationConfig;

    @Spy
    @InjectMocks
    private StudentService studentService;

    @Test
    void testAddStudentSuccess() {
        // Arrange
        Student student = new Student(0L, "John", "Doe", "12345");
        StudentDTO studentDTO = new StudentDTO(student);
        Student savedStudent = new Student(1L, "John", "Doe", "12345");

        // Mocking the necessary behaviors
        when(studentService.identifierAlreadyExists("12345")).thenReturn(false);
        when(studentService.prepareEntityObjectFromDTO(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(savedStudent);

        // Act
        Student result = studentService.addStudent(new StudentDTO(student));

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12345", result.getIdentifier());
        verify(studentRepository, times(1)).save(student);
        verify(studentService, times(1)).identifierAlreadyExists("12345");
    }
}


