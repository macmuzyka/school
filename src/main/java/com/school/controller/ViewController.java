package com.school.controller;

import com.school.service.ClassService;
import com.school.service.GradeService;
import com.school.service.StudentService;
import com.schoolmodel.model.dto.ClassWithListedStudentsDTO;
import com.schoolmodel.model.dto.StudentDTO;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/view")
public class ViewController {

    private final ClassService classService;
    private final StudentService studentService;
    private final GradeService gradeService;

    public ViewController(ClassService classService, StudentService studentService, GradeService gradeService) {
        this.classService = classService;
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

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
    public String addStudents(Model model,
                              @RequestParam(value = "studentId", required = false) Long studentId,
                              @RequestParam(value = "subjectName", required = false) String subjectName) {
        try {
            List<StudentSubjectGradesDTO> grades = gradeService.getSubjectGradesForStudent(studentId, subjectName);
            model.addAttribute("grades", grades);
            return "grade-list";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("")
    public String entrance() {
        try {
            return "entrance";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
