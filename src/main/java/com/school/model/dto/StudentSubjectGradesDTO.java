package com.school.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.school.model.CsvBean;

import java.util.List;


public class StudentSubjectGradesDTO extends CsvBean {
    @CsvBindByName(column = "Student Name")
    private String studentName;
    @CsvBindByName(column = "Subject")
    private String subject;
    @CsvBindByName(column = "ExamGrades")
    private List<GradeDisplayDTO> examGrades;
    @CsvBindByName(column = "TestGrades")
    private List<GradeDisplayDTO> testGrades;
    @CsvBindByName(column = "QuizGrades")
    private List<GradeDisplayDTO> quizGrades;
    @CsvBindByName(column = "QuestionGrades")
    private List<GradeDisplayDTO> questioningGrades;
    @CsvBindByName(column = "HomeworkGrades")
    private List<GradeDisplayDTO> homeworkGrades;
    @CsvBindByName(column = "OtherGrades")
    private List<GradeDisplayDTO> otherGrades;
    @CsvBindByName(column = "Average Grade")
    private String averageGrade;

    public StudentSubjectGradesDTO(String studentName, String subject, List<GradeDisplayDTO> examGrades, List<GradeDisplayDTO> testGrades, List<GradeDisplayDTO> quizGrades, List<GradeDisplayDTO> questioningGrades, List<GradeDisplayDTO> homeworkGrades, List<GradeDisplayDTO> otherGrades, String averageGrade) {
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

    public void setSubject(String subject) {
        this.subject = subject;
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

    public void setAverageGrade(String averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
