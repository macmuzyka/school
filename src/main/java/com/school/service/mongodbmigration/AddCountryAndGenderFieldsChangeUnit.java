package com.school.service.mongodbmigration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ChangeUnit(id = "V1_AddCountryAndGenderFields", order = "001")
public class AddCountryAndGenderFieldsChangeUnit {
    public static final Logger log = LoggerFactory.getLogger(AddCountryAndGenderFieldsChangeUnit.class);

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        Query countryQuery = new Query(Criteria.where("country").exists(false));
        Update countryUpdate = new Update().set("country", "US");
        Query genderQuery = new Query(Criteria.where("gender").exists(false));
        Update genderUpdate = new Update().set("gender", "unknown");
        mongoTemplate.updateMulti(countryQuery, countryUpdate, "students");
        mongoTemplate.updateMulti(genderQuery, genderUpdate, "students");
        log.info("✅ [V1_AddCountryAndGenderFields] Mongock migration executed: Added 'country and gender' fields where missing");
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        log.info("✅ [V1_AddCountryAndGenderFields] Executing mongock rollback");
        Update countryRollbackUpdate = new Update().unset("country");
        Update genderRollbackUpdate = new Update().unset("gender");
        mongoTemplate.updateMulti(new Query(), countryRollbackUpdate, "students");
        mongoTemplate.updateMulti(new Query(), genderRollbackUpdate, "students");
    }
}
