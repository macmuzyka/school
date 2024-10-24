package com.school.service.utils.mapper;

import com.schoolmodel.model.dto.ClassWithStudentsDTO;
import com.schoolmodel.model.dto.SubjectGradesDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryResultsMappingUtils {
    public static SubjectGradesDTO buildSubjectGradesObject(Object[] queryResult) {
        String studentName = (String) queryResult[0];
        String subject = (String) queryResult[1];
        String grades = (String) queryResult[2];
        BigDecimal average = (BigDecimal) queryResult[3];
        return new SubjectGradesDTO(studentName, subject, grades, average);
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
