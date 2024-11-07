package com.school.service.utils;

import com.schoolmodel.model.entity.Student;
import com.schoolmodel.model.entity.Subject;

public class FileNamePrefixResolver {
    private static final String SEPARATOR = "_";
    public static String build(Student foundStudent, Subject foundSubject) {
        String resolvedPrefix;
        if (foundStudent != null && foundSubject != null) {
            resolvedPrefix = foundStudent.getName() + SEPARATOR + foundStudent.getSurname() + SEPARATOR + foundSubject.getName() + SEPARATOR;
        } else if (foundStudent != null) {
            resolvedPrefix = foundStudent.getName() + SEPARATOR + foundStudent.getSurname() + SEPARATOR;
        } else if (foundSubject != null) {
            resolvedPrefix = foundSubject.getName() + SEPARATOR;
        } else {
            resolvedPrefix = "";
        }
        return resolvedPrefix;
    }
}
