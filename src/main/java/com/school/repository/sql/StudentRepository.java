package com.school.repository.sql;

import com.school.model.entity.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByCode(String code);

    List<Student> findAllByAssigned(boolean assigned);

    @Query(
            value = "SELECT * FROM student s " +
                    "WHERE " +
                    "(CAST(:studentId AS INT) IS NULL OR s.id = :studentId) AND " +
                    "(CAST(:studentName AS TEXT) IS NULL OR s.name ILIKE '%' || :studentName || '%') AND " +
                    "(CAST(:studentSurname AS TEXT) IS NULL OR s.surname ILIKE '%' || :studentSurname || '%') AND " +
                    "(CAST(:studentIdentifier AS TEXT) IS NULL OR s.identifier ILIKE '%' || :studentIdentifier || '%') AND " +
                    "s.assigned = true " +
                    "ORDER BY s.id ",
            nativeQuery = true
    )
    List<Student> findAllStudentsByParams(@Param("studentId") Long studentId,
                                          @Param("studentName") String studentName,
                                          @Param("studentSurname") String studentSurname,
                                          @Param("studentIdentifier") String studentIdentifier
    );

    @NotNull
    @Query("SELECT DISTINCT s FROM Student s " +
            "LEFT JOIN FETCH s.studentGrades " +
            "LEFT JOIN FETCH s.schoolClass")
    List<Student> findAll();
}
