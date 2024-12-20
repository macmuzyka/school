package com.school.model.dto

import com.school.model.entity.Student
import java.time.LocalDate

class StudentForListDTO(val name: String, val surname: String, val birthDate: LocalDate, val identifier: String) {
    constructor(student: Student) : this(
        name = student.name,
        surname = student.surname,
        birthDate = student.birthDate,
        identifier = student.identifier
    )

    companion object {
        fun StudentForListDTO.asSingleRow(index: Int): String {
            return "$index." + " " +
                    this.name + " " +
                    this.surname + " " +
                    this.birthDate + " " +
                    this.identifier
        }
    }
}