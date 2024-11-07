package com.school.service.utils;

public class QueryParamValidator {
    public static Long prepareLongValueForRepositoryQuery(String studentId) {
        if (Long.parseLong(studentId) == 0) {
            return null;
        } else {
            return Long.parseLong(studentId);
        }
    }

    public static long longValueFromRequestParameter(String requestNumericalParam) {
        return Long.parseLong(requestNumericalParam);
    }
}
