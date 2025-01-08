CREATE TABLE IF NOT EXISTS student
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    identifier VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE,
    assigned BOOLEAN NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    school_class_id BIGINT,
    CONSTRAINT fk_school_class
        FOREIGN KEY (school_class_id)
        REFERENCES school_class (id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS student_insert_error
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    identifier VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE,
    assigned BOOLEAN NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    error_cause TEXT,
    error_description TEXT,
    school_class_id BIGINT,
    CONSTRAINT fk_school_class
            FOREIGN KEY (school_class_id)
            REFERENCES school_class (id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS student_duplicate_error
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    identifier VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE,
    assigned BOOLEAN NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    error_cause TEXT,
    error_description TEXT,
    school_class_id BIGINT,
    CONSTRAINT fk_school_class
            FOREIGN KEY (school_class_id)
            REFERENCES school_class (id)
            ON DELETE SET NULL
);