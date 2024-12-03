package com.school.repository;

import com.school.model.entity.StudentInsertError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInsertErrorRepository extends JpaRepository<StudentInsertError, Long> {
}
