package com.school.model

import com.school.model.dto.GradeDisplayDTO

data class SubjectsWithGrades(
        val subjectId: Long,
        val subject: String,
        val examGrades: List<GradeDisplayDTO>? = emptyList(),
        val testGrades: List<GradeDisplayDTO>? = emptyList(),
        val quizGrades: List<GradeDisplayDTO>? = emptyList(),
        val questioningGrades: List<GradeDisplayDTO>? = emptyList(),
        val homeworkGrades: List<GradeDisplayDTO>? = emptyList(),
        val otherGrades: List<GradeDisplayDTO>? = emptyList(),
        val averageGrade: String? = ""
)