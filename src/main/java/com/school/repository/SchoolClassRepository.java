package com.school.repository;


import com.schoolmodel.model.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    @Query(value =
            "SELECT count(s.name) as number_of_students, sc.name, string_agg(s.name || ' ' || s.surname, ',')" +
                    "FROM student s " +
                    "RIGHT JOIN school_class sc ON s.school_class_id = sc.id " +
                    "GROUP BY sc.name " +
                    "ORDER BY count(s.name) ",
            nativeQuery = true)
    List<Object[]> findListedStudentsGroupedIntoClasses();

    Optional<SchoolClass> findSchoolClassByName(String className);

    @Query(
            value = "SELECT sub.id FROM subject sub " +
                    "JOIN school_class sc ON sub.school_class_id = sc.id " +
                    "JOIN student s ON s.school_class_id = sc.id " +
                    "WHERE s.code =:studentCodeFound ",
            nativeQuery = true
    )
    List<Long> findStudentClassSubjects(@Param("studentCodeFound") String studentCodeFound);

    @Query(
            value = "SELECT sc.id, count(s.id) " +
                    "FROM school_class sc JOIN " +
                    "student s ON sc.id = s.school_class_id " +
                    "GROUP BY sc.id " +
                    "HAVING count(s.id) < :maxClassSize " +
                    "ORDER BY count(s.id) " +
                    "LIMIT 1 ",
            nativeQuery = true
    )
    List<Object[]> findSchoolClassWithLessThanMaxSizeStudentsAndLowestCount(@Param("maxClassSize") int maxClassSize);
}
