package com.school.service.utils.mapper;

import com.schoolmodel.model.dto.ClassWithStudentCountDto;
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
        String average;
        try {
            average = castFromPostgresSqlDatabase(queryResult[4]);
        } catch (Exception e) {
            log.warn("Attempting other cast, as primary failed..");
            average = castFromH2InMemoryDatabase(queryResult[4]);
            log.info("Cast success");

        }
        return new StudentSubjectGradesDTO(name + " " + surname, subject, grades, average);
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
        List<String> studentNames;
        if (queryResult[2] == null) {
            studentNames = Collections.emptyList();
        } else {
            studentNames = Arrays.asList(((String) queryResult[2]).split(","));
        }
        return new ClassWithListedStudentsDTO(studentCount, className, studentNames);
    }

    public static ClassWithStudentCountDto buildClassWithStudentCount(Object[] queryResult) {
        long classId = (long) queryResult[0];
        long studentCount = (long) queryResult[1];
        return new ClassWithStudentCountDto(classId, studentCount);
    }
}
