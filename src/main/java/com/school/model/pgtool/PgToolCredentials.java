package com.school.model.pgtool;

import org.springframework.beans.factory.annotation.Value;

abstract public class PgToolCredentials {
    @Value("#{'${spring.datasource.url}'.split('/')[3]}")
    protected String databaseName;
    @Value("${spring.datasource.password}")
    protected String databasePassword;
}
