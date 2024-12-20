package com.school.service.utils.mapper;

import com.school.model.SubjectsWithGrades;
import com.school.model.dto.ClassWithListedStudentsDTO;
import com.school.model.dto.GradeDisplayDTO;
import com.school.model.dto.StudentSubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryResultsMapper {
    private static final Logger log = LoggerFactory.getLogger(QueryResultsMapper.class);

    public static StudentSubjectGradesDTO buildStudentSubjectGradesObject(Object[] queryResult) {
        String name = (String) queryResult[0];
        String surname = (String) queryResult[1];
        String subject = (String) queryResult[2];
        List<GradeDisplayDTO> examGrades = checkForEmptyGrades(queryResult[3]);
        List<GradeDisplayDTO> testGrades = checkForEmptyGrades(queryResult[4]);
        List<GradeDisplayDTO> quizGrades = checkForEmptyGrades(queryResult[5]);
        List<GradeDisplayDTO> questioningGrades = checkForEmptyGrades(queryResult[6]);
        List<GradeDisplayDTO> homeworkGrades = checkForEmptyGrades(queryResult[7]);
        List<GradeDisplayDTO> otherGrades = checkForEmptyGrades(queryResult[8]);
        String average = castAverage(queryResult[9]);

        return new StudentSubjectGradesDTO(name + " " + surname, subject, examGrades, testGrades, quizGrades, questioningGrades, homeworkGrades, otherGrades, average);
    }

    private static String castAverage(Object average) {
        if (average == null) {
            return "0.0";
        } else {
            try {
                return castFromPostgresSqlDatabase(average);
            } catch (Exception e) {
                log.warn("Attempting other cast, as primary failed..");
                return castFromH2InMemoryDatabase(average);
            }
        }

    }

    private static String castFromPostgresSqlDatabase(Object queryResultObject) {
        return String.valueOf(queryResultObject);
    }

    private static String castFromH2InMemoryDatabase(Object queryResultObject) {
        return String.valueOf((double) queryResultObject);
    }

    public static ClassWithListedStudentsDTO buildClassWithListedStudents(Object[] queryResult) {
        long studentCount = (long) queryResult[0];
        String className = (String) queryResult[1];
        List<String> students;
        if (queryResult[2] == null) {
            students = Collections.emptyList();
        } else {
            students = Arrays.asList(((String) queryResult[2]).split(","));
        }
        return new ClassWithListedStudentsDTO(studentCount, className, students);
    }

    public static SubjectsWithGrades prepareGradesForSingleStudent(Object[] result) {
        long subjectId = (long) result[0];
        String subject = (String) result[1];
        List<GradeDisplayDTO> examGrades = checkForEmptyGrades(result[2]);
        List<GradeDisplayDTO> testGrades = checkForEmptyGrades(result[3]);
        List<GradeDisplayDTO> quizGrades = checkForEmptyGrades(result[4]);
        List<GradeDisplayDTO> questioningGrades = checkForEmptyGrades(result[5]);
        List<GradeDisplayDTO> homeworkGrades = checkForEmptyGrades(result[6]);
        List<GradeDisplayDTO> otherGrades = checkForEmptyGrades(result[7]);
        String average = castAverage((result[8]));

        return new SubjectsWithGrades(subjectId, subject, examGrades, testGrades, quizGrades, questioningGrades, homeworkGrades, otherGrades, average);
    }

    private static List<GradeDisplayDTO> checkForEmptyGrades(Object grades) {
        if (grades == null) {
            List<GradeDisplayDTO> empty = new ArrayList<>();
            empty.add(new GradeDisplayDTO("-", 0L, "black"));
            return empty;
        } else {
            return Arrays.stream(((String) grades).split(","))
                    .map(dashSeparated -> new GradeDisplayDTO(dashSeparated.trim().split("\\^")))
                    .toList();
        }
    }

    //TODO: do the same for students list
    public static List<StudentSubjectGradesDTO> getEmptyGradesListIndicator() {
        return List.of(new StudentSubjectGradesDTO("---",
                "---",
                List.of(new GradeDisplayDTO("", 0L, "black")),
                List.of(new GradeDisplayDTO("", 0L, "black")),
                List.of(new GradeDisplayDTO("", 0L, "black")),
                List.of(new GradeDisplayDTO("", 0L, "black")),
                List.of(new GradeDisplayDTO("", 0L, "black")),
                List.of(new GradeDisplayDTO("", 0L, "black")),
                "---")
        );
    }
}
