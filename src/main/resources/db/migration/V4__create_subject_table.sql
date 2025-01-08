CREATE TABLE IF NOT EXISTS subject
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    school_class_id BIGINT NOT NULL,
    CONSTRAINT fk_subject_school_class
        FOREIGN KEY (school_class_id)
        REFERENCES school_class (id)
        ON DELETE CASCADE
);