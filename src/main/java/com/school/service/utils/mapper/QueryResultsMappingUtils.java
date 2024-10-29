package com.school.service.utils.mapper;

import com.schoolmodel.model.dto.ClassWithStudentsDTO;
import com.schoolmodel.model.dto.SubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryResultsMappingUtils {
    private static final Logger log = LoggerFactory.getLogger(QueryResultsMappingUtils.class);

    public static SubjectGradesDTO buildSubjectGradesObject(Object[] queryResult) {
        String id = String.valueOf((long) queryResult[0]);
        String name = (String) queryResult[1];
        String surname = (String) queryResult[2];
        String subject = (String) queryResult[3];
        String grades = (String) queryResult[4];
        String average;
        try {
            average = String.valueOf((BigDecimal) queryResult[5]);
        } catch (Exception e) {
            log.warn("Probably other cast needed");
            average = String.valueOf((double) queryResult[5]);
        }
        return new SubjectGradesDTO(id + ". " + name + " " + surname, subject, grades, average);
    }

    public static ClassWithStudentsDTO buildClassWithStudentsObject(Object[] queryResult) {
        long studentCount = (long) queryResult[0];
        String className = (String) queryResult[1];
        List<String> studentNames;
        if (queryResult[2] == null) {
            studentNames = Collections.emptyList();
        } else {
            studentNames = Arrays.asList(((String) queryResult[2]).split(","));
        }
        return new ClassWithStudentsDTO(studentCount, className, studentNames);
    }
}
