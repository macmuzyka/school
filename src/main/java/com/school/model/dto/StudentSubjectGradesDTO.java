package com.school.model.dto;

import com.school.model.CsvBean;

import java.util.List;


public class StudentSubjectGradesDTO extends CsvBean {
    private final String studentName;
    private final String subject;
    private final List<GradeDisplayDTO> examGrades;
    private final List<GradeDisplayDTO> testGrades;
    private final List<GradeDisplayDTO> quizGrades;
    private final List<GradeDisplayDTO> questioningGrades;
    private final List<GradeDisplayDTO> homeworkGrades;
    private final List<GradeDisplayDTO> otherGrades;
    private final String averageGrade;

    public StudentSubjectGradesDTO(String studentName,
                                   String subject,
                                   List<GradeDisplayDTO> examGrades,
                                   List<GradeDisplayDTO> testGrades,
                                   List<GradeDisplayDTO> quizGrades,
                                   List<GradeDisplayDTO> questioningGrades,
                                   List<GradeDisplayDTO> homeworkGrades,
                                   List<GradeDisplayDTO> otherGrades,
                                   String averageGrade
    ) {
        this.studentName = studentName;
        this.subject = subject;
        this.examGrades = examGrades;
        this.testGrades = testGrades;
        this.quizGrades = quizGrades;
        this.questioningGrades = questioningGrades;
        this.homeworkGrades = homeworkGrades;
        this.otherGrades = otherGrades;
        this.averageGrade = averageGrade;
    }

    public String getSubject() {
        return subject;
    }

    public List<GradeDisplayDTO> getExamGrades() {
        return examGrades;
    }

    public List<GradeDisplayDTO> getTestGrades() {
        return testGrades;
    }

    public List<GradeDisplayDTO> getQuizGrades() {
        return quizGrades;
    }

    public List<GradeDisplayDTO> getQuestioningGrades() {
        return questioningGrades;
    }

    public List<GradeDisplayDTO> getHomeworkGrades() {
        return homeworkGrades;
    }

    public List<GradeDisplayDTO> getOtherGrades() {
        return otherGrades;
    }

    public String getAverageGrade() {
        return averageGrade;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        return "StudentSubjectGradesDTO{" +
                "studentName='" + studentName + '\'' +
                ", subject='" + subject + '\'' +
                ", examGrades='" + examGrades + '\'' +
                ", testGrades='" + testGrades + '\'' +
                ", quizGrades='" + quizGrades + '\'' +
                ", questioningGrades='" + questioningGrades + '\'' +
                ", homeworkGrades='" + homeworkGrades + '\'' +
                ", otherGrades='" + otherGrades + '\'' +
                ", averageGrade='" + averageGrade + '\'' +
                '}';
    }
}
