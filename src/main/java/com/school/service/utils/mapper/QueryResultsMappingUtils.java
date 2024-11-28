package com.school.service.utils.mapper;

import com.school.model.SubjectsWithGrades;
import com.schoolmodel.model.dto.ClassWithListedStudentsDTO;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryResultsMappingUtils {
    private static final Logger log = LoggerFactory.getLogger(QueryResultsMappingUtils.class);

    public static StudentSubjectGradesDTO buildStudentSubjectGradesObject(Object[] queryResult) {
        String name = (String) queryResult[0];
        String surname = (String) queryResult[1];
        String subject = (String) queryResult[2];
        String grades = (String) queryResult[3];
        String average = castAverage(queryResult[4]);

        return new StudentSubjectGradesDTO(name + " " + surname, subject, grades, average);
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
        String grades = checkForEmptyGrades(result[2]);
        String average = castAverage((result[3]));

        return new SubjectsWithGrades(subjectId, subject, grades, average);
    }

    private static String checkForEmptyGrades(Object grades) {
        if (grades == null) {
            return "- - - No grades yet - - -";
        } else {
            return (String) grades;
        }
    }
}
