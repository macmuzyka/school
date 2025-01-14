package com.school.model

import com.school.model.entity.SchoolClass

data class SchoolClassDTO(
    val id: Long,
    val className: String,
    val students: List<String>,
    val subjects: List<String>
) {
    constructor(schoolClass: SchoolClass): this(
        id = schoolClass.id,
        className = schoolClass.className(),
        students = schoolClass.classStudents.map { it.name },
        subjects = schoolClass.classSubjects.map { it.name }
    )
}
