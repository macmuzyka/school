package com.school.service

import org.springframework.stereotype.Service

@Service
class RoadMapService {
    fun getCurrentTodos(): MutableMap<String, List<String>> {
        return mutableMapOf(
            "Done" to listOf(
                "Project current version displayed on dashboard",
                "Seeding grade upon insertion from list is optional, but only available if there are not added grades",
                "Better navigation to previous page (history based), but added button to go upper in page hierarchy",
                "All notification popups sent from backend web-socket to be captured, consumed & displayed using frontend websocket",
                "Feedback collecting via form in GUI & sent via prepared mail to project mailbox " +
                        "& central registry to store feedback content & provider",
                "Upon insertion student list errors inserts showing up in GUI via notification",
                "Enriched grade entity with note & grade category",
                "Last five hints about grade description (subject based) to be picked from dedicated toolbar",
                "In detailed student view, when hovered over grade more details & information is shown about " +
                        "the grade that was added upon grade adding",
                "Returned produces files about the grades changes to fit displaying grades grouped into " +
                        "grade categories",
                "Student list to be imported as text file"
            ),
            "Backend" to listOf(
                "Should duplicate detected upon student adding be added to duplicated students list?.. ",
                "Student list input to be of different file type not only text file -> .xls",
                "Road map to be stored in db & roadmap stages changed based on work progress & released newsletter " +
                        "if DONE -> list sent via newsletter and moved to 'old' done's as archive",
                "Should removing student be moving them to other table, or just change student status -> changes in student table/entity",
                "Teacher entity with assigned subject -> only teacher of given subject can add grade to this subject",
                "Weighted grades (some grades are worth more than others, thus counting average grade will be different)"
            ),
            "Frontend" to listOf(
                "Adjust timing of notification showing and page refreshing for all views",
                "Redirection to proper pages upon detecting student duplicate & student insert error",
                "Frontend for Road Map in school informer central registry for managing feedback and new version release propagation",
                "Confirmation popup form instead of alerts/web popups",
                "Unifying view, for example, wherever student record is visible, make redirection to student details & similar",
                "GUI for adding class & adding student to a class",
                "Draggable students among classes to assign students to different class",
            ),
            "Backend + Frontend verification" to listOf(
                "Roadmap aspect of application moved to central registry (school-informer)",
                "Database backup & restore via GUI",
                "Application configuration via GUI, config like available grades, class size etc",
                "Implement logging with visibility scope (teacher, student, legal guardian)",
                "Legal guardian entity with assigned student (visible grades of student that is associated with guardian)"
            )
        )
    }
}