package com.school.repository.sql;


import com.school.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findFirstByName(String name);
    List<Subject> findBySchoolClassId(Long id);
}
