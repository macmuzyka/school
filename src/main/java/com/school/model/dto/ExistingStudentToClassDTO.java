package com.school.model.dto;

import com.school.model.enums.ClassAction;

public class ExistingStudentToClassDTO {
    private final String studentCode;
    private final long schoolClassId;
    private final ClassAction action;

    public ExistingStudentToClassDTO(String studentCode, long schoolClassId, ClassAction action) {
        this.studentCode = studentCode;
        this.schoolClassId = schoolClassId;
        this.action = action;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public long getSchoolClassId() {
        return schoolClassId;
    }

    public ClassAction getAction() {
        return action;
    }

    public String component1() {
        return studentCode;
    }

    public long component2() {
        return schoolClassId;
    }

    public ClassAction component3() {
        return action;
    }
}
