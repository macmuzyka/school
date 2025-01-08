CREATE TABLE IF NOT EXISTS school_class
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    school_id BIGINT NOT NULL,
    CONSTRAINT fk_school_class_school
        FOREIGN KEY (school_id)
        REFERENCES school (id)
        ON DELETE CASCADE
);