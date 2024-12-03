package com.school.service

import org.springframework.stereotype.Service

@Service
class PseudoRoadMapService {
    fun getCurrentTodos(): MutableMap<String, List<String>> {
        return mutableMapOf(
                "Done" to listOf(
                        "Simple frontend for application with spring + mvc to view collected student, classes & grades",
                        "When devel profile is chosen, saving insert time of random grades to database for different count of grades and " +
                                "database type (H2 or PostgresSQL) - for measured time compare, statistics & possible differences shown on graphs"
                ),
                "Backend" to listOf(
                        "Student list input to be of different file type not only text file -> .xls",
                        "Enriched grade entity with note or description",
                        "Teacher entity with assigned subject -> only teacher of given subject can add grade to this subject",
                        "Weighted grades (some grades are worth more than others, thus counting average grade will be different)"
                ),
                "Frontend" to listOf(
                        "All notification popups to be send to frontend via web-socket",
                        "GUI for adding class & adding student to a class",
                        "Draggable students among classes to assign students to different class",
                        "When hovered over grade it shows more details & information about the grade, when it was added, what grade relates to etc"
                ),
                "Backend + Frontend verification" to listOf(
                        "Feedback collecting via form in GUI send to prepared mail",
                        "Upon insertion student list showing up records that error occurred",
                        "Better navigation to previous page",
                        "Application configuration via GUI, config like available grades, subjects, class size etc",
                        "Implement logging with visibility scope (teacher, student, legal guardian)",
                        "Legal guardian entity with assigned student (visible grades of student that is associated with guardian)"
                ),
        )
    }
}