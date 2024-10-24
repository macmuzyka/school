package com.school.service;

import com.schoolmodel.model.dto.SubjectGradesDTO;
import com.school.repository.GradeRepository;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.school.service.utils.mapper.QueryResultsMappingUtils;
import com.schoolmodel.model.entity.Grade;
import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.entity.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    private static final Logger log = LoggerFactory.getLogger(GradeService.class);
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    public Grade addGrade(Grade grade) {
        log.info("Adding grade {} to student {} to subject {}", grade.getGradeValue(), grade.getStudent(), grade.getSubject());
        return gradeRepository.save(grade);
    }

    public Grade addGrade(int grade, String subject, String studentCode) {
        log.info("Adding grade {} to student's code {} to subject {}", grade, studentCode, subject);
        Optional<Subject> subjectFound = subjectRepository.findFirstByName(subject);
        if (subjectFound.isPresent()) {
            Optional<Student> studentFound = studentRepository.findStudentByCode(studentCode);
            if (studentFound.isPresent()) {
                log.info(studentFound.get().getName() + " " + studentFound.get().getSurname() + " found based on code!");
                if (grade > 0 && grade <= 6) {
                    return gradeRepository.save(new Grade(
                            grade,
                            studentFound.get(),
                            subjectFound.get())
                    );
                } else {
                    String message = "Assigning " + grade + " blocked as it is improver value!";
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }
            } else {
                String message = "Cannot find " + studentCode + " student code to assign grade to!";
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        } else {
            String message = "Cannot find " + subject + " subject to assign grade to!";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public List<SubjectGradesDTO> getSubjectGradesForStudent(Long studentId, String subjectName) {
        log.info("Getting grades for students grouped by subjects..");
        List<Object[]> results = gradeRepository.findAllGradesGroupedBySubject(studentId, subjectName);

        try {
            return results.stream()
                    .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                    // TODO: useless comparator for now as db query is supposed to be faster
                    //  -> might use more complex comparator in the future
                    //.sorted(SubjectGradesDTO.compareAverageGrade)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }
}
