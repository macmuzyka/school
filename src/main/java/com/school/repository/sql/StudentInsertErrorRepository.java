package com.school.repository.sql;

import com.school.model.entity.StudentInsertError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentInsertErrorRepository extends JpaRepository<StudentInsertError, Long> {
    Optional<StudentInsertError> findStudentInsertErrorByIdentifier(String identifier);
}
