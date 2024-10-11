package com.school.repository;


import com.schoolmodel.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    @Query(value = "SELECT s.name, sub.name AS subject, " +
            "string_agg(g.grade_value::text, ', ') AS grades," +
            "ROUND(AVG(g.grade_value), 2) AS average " +
            "FROM grade g " +
            "INNER JOIN student s ON g.student_id = s.id " +
            "INNER JOIN subject sub ON g.subject_id = sub.id " +
            "WHERE (:studentId IS NULL OR s.id = :studentId) " +
            "GROUP BY s.name, sub.name",
            nativeQuery = true
    )
    List<Object[]> findAllGradesGroupedBySubject(@Param("studentId") Long studentId);
}
