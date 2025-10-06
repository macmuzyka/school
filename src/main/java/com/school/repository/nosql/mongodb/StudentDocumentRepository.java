package com.school.repository.nosql.mongodb;

import com.school.model.nosql.mongodb.StudentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDocumentRepository extends MongoRepository<StudentDocument, String> {
}