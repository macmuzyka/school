package com.school.repository;


import com.schoolmodel.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findSubjectByName(String name);
    Optional<Subject> findFirstByName(String name);
}
