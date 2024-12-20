package com.school.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.school.model.CsvBean;

import java.util.stream.Collectors;

public class StudentSubjectGradesCsvBeanDTO extends CsvBean {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "Student Name")
    private final String studentName;
    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "Subject")
    private final String subject;
    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "ExamGrades")
    private final String examGrades;
    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "TestGrades")
    private final String testGrades;
    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "QuizGrades")
    private final String quizGrades;
    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "QuestionGrades")
    private final String questioningGrades;
    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "HomeworkGrades")
    private final String homeworkGrades;
    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "OtherGrades")
    private final String otherGrades;
    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "Average Grade")
    private final String averageGrade;

    public StudentSubjectGradesCsvBeanDTO(StudentSubjectGradesDTO groupedGrades) {
        this.studentName = groupedGrades.getStudentName();
        this.subject = groupedGrades.getSubject();
        this.examGrades = groupedGrades.getExamGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.testGrades = groupedGrades.getTestGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.quizGrades = groupedGrades.getQuizGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.questioningGrades = groupedGrades.getQuestioningGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.homeworkGrades = groupedGrades.getHomeworkGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.otherGrades = groupedGrades.getOtherGrades().stream().map(GradeDisplayDTO::getGrade).collect(Collectors.joining(","));
        this.averageGrade = groupedGrades.getAverageGrade();
    }

    String getStudentName() {
        return studentName;
    }

    String getSubject() {
        return subject;
    }

    String getExamGrades() {
        return examGrades;
    }

    String getTestGrades() {
        return testGrades;
    }

    String getQuizGrades() {
        return quizGrades;
    }

    String getQuestioningGrades() {
        return questioningGrades;
    }

    String getHomeworkGrades() {
        return homeworkGrades;
    }

    String getOtherGrades() {
        return otherGrades;
    }

    String getAverageGrade() {
        return averageGrade;
    }
}
