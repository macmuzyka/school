package com.school.controller;

import com.school.configuration.ApplicationConfig;
import com.school.model.OptionalRequestParams;
import com.school.model.ProjectVersion;
import com.school.model.SubjectsWithGrades;
import com.school.model.dto.ClassWithListedStudentsDTO;
import com.school.model.dto.StudentDTO;
import com.school.model.dto.StudentSubjectGradesDTO;
import com.school.service.*;
import com.school.service.classschedule.ClassScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.school.service.utils.RequestParamValidator.prepareOptionalRequestParams;

@Controller
@RequestMapping("/school")
public class ModelViewController {

    private final ClassService classService;
    private final StudentService studentService;
    private final DuplicatedStudentService duplicatedStudentService;
    private final InsertErrorStudentService insertErrorStudentService;
    private final GradeService gradeService;
    private final ClassScheduleService classScheduleService;
    private final RoadMapService roadMapService;
    private final ProjectVersionService projectVersionService;
    private final ApplicationValidityService applicationValidityService;
    private final ApplicationConfig applicationConfig;

    public ModelViewController(ClassService classService,
                               StudentService studentService,
                               DuplicatedStudentService duplicatedStudentService,
                               InsertErrorStudentService insertErrorStudentService,
                               GradeService gradeService,
                               SeedGradesService seedGradesService,
                               ClassScheduleService classScheduleService,
                               RoadMapService roadMapService,
                               ProjectVersionService projectVersionService,
                               ApplicationValidityService applicationValidityService,
                               ApplicationConfig applicationConfig
    ) {
        this.classService = classService;
        this.studentService = studentService;
        this.duplicatedStudentService = duplicatedStudentService;
        this.insertErrorStudentService = insertErrorStudentService;
        this.gradeService = gradeService;
        this.classScheduleService = classScheduleService;
        this.roadMapService = roadMapService;
        this.projectVersionService = projectVersionService;
        this.applicationValidityService = applicationValidityService;
        this.applicationConfig = applicationConfig;
    }

    private final Logger log = LoggerFactory.getLogger(ModelViewController.class);

    @GetMapping("/students")
    public String getStudents(Model model,
                              @RequestParam(value = "assigned", required = false) Boolean assignedParam) {
        try {
            List<StudentDTO> students = studentService.getAllStudents(assignedParam);
            model.addAttribute("students", students);
            return "student-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/find")
    public String findStudents(Model model,
                               @RequestParam(required = false) Long id,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String surname,
                               @RequestParam(required = false) String identifier) {
        try {
            OptionalRequestParams params = prepareOptionalRequestParams(null, id, name, surname, identifier, null);
            List<StudentDTO> studentsFound = studentService.findStudents(params);

            model.addAttribute("studentsFound", studentsFound);
            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("surname", surname);
            model.addAttribute("identifier", identifier);
            return "add-student-grade";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/classes")
    public String getClassesWithStudents(Model model) {
        try {
            List<ClassWithListedStudentsDTO> classes = classService.getClassesWithStudents();
            model.addAttribute("classes", classes);
            return "class-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/subject-grades")
    public String getSubjectGrades(Model model,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String surname,
                                   @RequestParam(required = false) String identifier,
                                   @RequestParam(required = false) String subject) {
        try {
            if (("---").equals(subject)) {
                subject = null;
            }
            OptionalRequestParams params = prepareOptionalRequestParams(null, id, name, surname, identifier, subject);
            List<StudentSubjectGradesDTO> grades = gradeService.getSubjectGradesForStudents(params);

            model.addAttribute("grades", grades);
            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("surname", surname);
            model.addAttribute("identifier", identifier);
            model.addAttribute("subject", subject);
            List<String> subjectsWithClearParameter = new ArrayList<>(applicationConfig.getAvailableSubjects());
            subjectsWithClearParameter.add("---");
            model.addAttribute("subjects", subjectsWithClearParameter);
            return "grade-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/duplicates")
    public String getDuplicates(Model model) {
        try {
            List<StudentDTO> duplicatedStudents = duplicatedStudentService.getAllDuplicatedStudents();
            model.addAttribute("duplicates", duplicatedStudents);
            return "duplicated-student-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/insert-errors")
    public String getInsertErrors(Model model) {
        try {
            List<StudentDTO> insertErrors = insertErrorStudentService.getInputErrorStudents();
            model.addAttribute("insertErrors", insertErrors);
            return "insert-error-student-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/add-students")
    public String addStudents() {
        try {
            return "add-students";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping
    public String entryPoint(Model model) {
        try {
            ProjectVersion pv = projectVersionService.getCurrentProjectVersion();
            String validity = applicationValidityService.getApplicationValidity().name().replace("_", " ");
            model.addAttribute("projectVersion", pv);
            model.addAttribute("applicationValidity", validity);
            return "entry_points/entry-point";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/student-entry-point")
    public String studentEntryPoint() {
        try {
            return "entry_points/student-entry-point";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/grades-entry-point")
    public String gradesEntryPoint() {
        try {
            return "entry_points/grades-entry-point";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/schedules-entry-point")
    public String schedulesEntryPoint(Model model) {
        try {
            model.addAttribute("classes", classService.getClassesDTOs());
            return "entry_points/schedules-entry-point";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/schedule-details")
    public String schedulesList(Model model,
                                @RequestParam("id") Long id) {
        try {
            model.addAttribute("classId", id);
            model.addAttribute("timetable", classScheduleService.classScheduleGroupedByDaySubjectAndTimeframe(id, true));
            model.addAttribute("days", EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
            return "schedule-details";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/add-student-grade")
    public String addGrade(Model model) {
        try {
            List<ClassWithListedStudentsDTO> classes = classService.getClassesWithStudents();
            model.addAttribute("classes", classes);
            return "add-student-grade";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/student-details")
    public String getStudentDetails(Model model,
                                    @RequestParam("id") Long id) {
        try {
            StudentDTO foundStudent = new StudentDTO(studentService.getStudent(id));
            List<SubjectsWithGrades> subjectsGrades = gradeService.getSubjectGradesForStudent(id);
            model.addAttribute("id", id);
            model.addAttribute("student", foundStudent);
            model.addAttribute("subjectsGrades", subjectsGrades);
            model.addAttribute("grades", applicationConfig.getAvailableGrades());
            model.addAttribute("gradeTypes", applicationConfig.getGradeTypes());
            return "student-details";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/todo")
    public String getTodo(Model model) {
        Map<String, List<String>> thingsTodo = roadMapService.getCurrentRoadmap();
        model.addAttribute("thingsTodo", thingsTodo);
        return "todo-list";
    }

    @GetMapping("/feedback")
    public String getFeedbackPage() {
        return "feedback";
    }

    @GetMapping("/database-backup")
    public String getBackup() {
        return "backup";
    }
}
