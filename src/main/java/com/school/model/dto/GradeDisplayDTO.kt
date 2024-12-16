package com.school.model.dto

data class GradeDisplayDTO(val grade: String,
                           val id: Long,
                           val color: String = resolveColor(grade)
) {
    companion object {
        fun resolveColor(grade: String): String {
            return when (grade.toLong()) {
                6L -> "limegreen"
                5L -> "green"
                4L -> "darkblue"
                3L -> "mediumpurple"
                2L -> "orange"
                1L -> "red"
                else -> "black"
            }
        }
    }

    constructor(gradePair: Array<String>) : this(
            grade = gradePair[0],
            id = gradePair[1].toLong()
    )
}
