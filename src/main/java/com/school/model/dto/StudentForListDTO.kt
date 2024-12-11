package com.school.model.dto

import com.school.model.entity.Student
import java.time.LocalDate

class StudentForListDTO(val indexWithDot: Long, val name: String, val surname: String, val birthDate: LocalDate, val identifier: String) {
    constructor(student: Student) : this(
            indexWithDot = student.id,
            name = student.name,
            surname = student.surname,
            birthDate = student.birthDate,
            identifier = student.identifier
    )

    companion object {
        fun StudentForListDTO.asSingleRow(): String {
            return "${this.indexWithDot}." + " " +
                    this.name + " " +
                    this.surname + " " +
                    this.birthDate + " " +
                    this.identifier
        }
    }
}