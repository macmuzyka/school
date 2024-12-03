package com.school.repository;


import com.school.model.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    @Query(value =
            "SELECT s.name, s.surname, sub.name AS subject, " +
                    "string_agg(g.grade_value::text, ', ') AS grades," +
                    "ROUND(AVG(g.grade_value), 2) AS average " +
            "FROM grade g " +
            "INNER JOIN student s ON g.student_id = s.id " +
            "INNER JOIN subject sub ON g.subject_id = sub.id " +
            "WHERE " +
                    "(CAST(:studentId AS INT) IS NULL OR s.id = :studentId) AND " +
                    "(CAST(:subjectName AS TEXT) IS NULL OR sub.name ILIKE '%' || :subjectName || '%') AND " +
                    "(CAST(:studentName AS TEXT) IS NULL OR s.name ILIKE '%' || :studentName || '%') AND " +
                    "(CAST(:studentSurname AS TEXT) IS NULL OR s.surname ILIKE '%' || :studentSurname || '%') AND " +
                    "(CAST(:studentIdentifier AS TEXT) IS NULL OR s.identifier ILIKE '%' || :studentIdentifier || '%')  " +
            "GROUP BY s.id, s.name, s.surname, sub.name " +
            "ORDER BY average DESC",
            nativeQuery = true
    )
    List<Object[]> findAllGradesGroupedBySubject(@Param("studentId") Long studentId,
                                                 @Param("subjectName") String subjectName,
                                                 @Param("studentName") String studentName,
                                                 @Param("studentSurname") String studentSurname,
                                                 @Param("studentIdentifier") String studentIdentifier
    );

    @Query(value =
            "SELECT sub.id AS subject_id, " +
                    "sub.name AS subject, " +
                    "string_agg(CASE WHEN s.id = :studentId THEN g.grade_value::text END, ', ') AS grades," +
                    "ROUND(AVG(CASE WHEN s.id = :studentId THEN g.grade_value END), 2) AS average " +
            "FROM school_class sc " +
            "JOIN subject sub ON sc.id = sub.school_class_id " +
            "LEFT JOIN grade g ON g.subject_id = sub.id " +
            "LEFT JOIN student s ON g.student_id = s.id " +
            "WHERE sc.id IN (" +
                    "SELECT DISTINCT sc.id  " +
                    "FROM school_class sc " +
                    "JOIN student s ON sc.id = s.school_class_id " +
                    "WHERE s.id = :studentId " +
                    ") " +
            "GROUP BY sc.id, sub.id, sub.name " +
            "ORDER BY average DESC;",
            nativeQuery = true
    )
    List<Object[]> findAllGradesGroupedBySubjectForSingleStudent(@Param("studentId") Long studentId);
}
