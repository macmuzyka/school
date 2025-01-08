package com.school.model.pgtool;

import org.springframework.beans.factory.annotation.Value;

abstract public class PgToolCredentials {
    //TODO: causing trouble when using devel profile (H2 database),
    // datasource.url is then jdbc:h2:mem:testdb, change to a file or find other solution
    //@Value("#{'${spring.datasource.url}'.split('/')[3]}")
    @Value("${database-name}")
    protected String databaseName;
    @Value("${spring.datasource.password}")
    protected String databasePassword;
}
