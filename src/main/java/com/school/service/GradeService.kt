package com.school.service;

import com.school.model.OptionalRequestParams;
import com.school.model.SubjectsWithGrades;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
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

    //TODO: refactor code to be more readable
    public Grade addGrade(int grade, Long subjectId, Long studentId) {
        log.info("Attempting to add grade {} to student's id {} to subjectId {}", grade, studentId, subjectId);
        Optional<Subject> subjectFound = subjectRepository.findById(subjectId);
        try {
            if (subjectFound.isPresent()) {
                Optional<Student> studentFound = studentRepository.findById(studentId);
                if (studentFound.isPresent()) {
                    log.info(studentFound.get().getName() + " " + studentFound.get().getSurname() + " found based passed id [" + studentFound + "]!");
                    if (grade > 0 && grade <= 6) {
                        Grade savedGrade = gradeRepository.save(new Grade(
                                grade,
                                studentFound.get(),
                                subjectFound.get())
                        );
                        log.debug("savedGrade");
                        log.debug(savedGrade.toString());
                        log.info("[Grade added!]");
                    } else {
                        String message = grade + " is improver value! Not going to be added";
                        log.error(message);
                        throw new IllegalArgumentException(message);
                    }
                } else {
                    String message = "Cannot find " + studentId + " student id to assign grade to!";
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }
            } else {
                String message = "Cannot find " + subjectId + " subject to assign grade to!";
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<StudentSubjectGradesDTO> getSubjectGradesForStudents(OptionalRequestParams params) {
        log.info("Getting grades for students grouped by subjects..");
        log.info("Params: {}", params);
        List<Object[]> results = gradeRepository.findAllGradesGroupedBySubject(
                params.getId(),
                params.getSubject(),
                params.getName(),
                params.getSurname(),
                params.getIdentifier()
        );

        try {
            List<StudentSubjectGradesDTO> grades = results.stream()
                    .map(QueryResultsMappingUtils::buildStudentSubjectGradesObject)
                    .toList();
            log.warn("Number of objects found : {}", grades.size());
            return populateWithExampleRecordIfListEmpty(grades);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    public List<SubjectsWithGrades> getSubjectGradesForStudent(Long studentId) {
        log.debug("getSubjectGradesForStudent :: Student id param: {}", studentId);
        return gradeRepository.findAllGradesGroupedBySubjectForSingleStudent(studentId).stream()
                .map(QueryResultsMappingUtils::prepareGradesForSingleStudent)
                .toList();
    }

    private List<StudentSubjectGradesDTO> populateWithExampleRecordIfListEmpty(List<StudentSubjectGradesDTO> grades) {
        if (grades.isEmpty()) {
            return List.of(new StudentSubjectGradesDTO("No", "grades", "to", "display"));
        } else {
            return grades;
        }
    }
}
