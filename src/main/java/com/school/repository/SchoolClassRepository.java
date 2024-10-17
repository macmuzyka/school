package com.school.repository;


import com.schoolmodel.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    //    @Query(value = "SELECT count(s.name) as number_of_students, sc.name, string_agg((s.name)::text, ',') " +
//            "FROM student s " +
//            "JOIN school_class sc ON s.school_class_id = sc.id " +
//            "GROUP BY sc.name", nativeQuery = true)
//    List<Object[]> findStudentsGroupedIntoClasses();
    @Query(value =
            "SELECT count(s.name) as number_of_students, sc.name, string_agg(s.name || ' ' || s.surname, ',')" +
                    "FROM student s " +
                    "JOIN school_class sc ON s.school_class_id = sc.id " +
                    "GROUP BY sc.name",
            nativeQuery = true)
    List<Object[]> findStudentsGroupedIntoClasses();

    Optional<SchoolClass> findSchoolClassByName(String className);
}
