CREATE TABLE IF NOT EXISTS grade
(
    id BIGSERIAL PRIMARY KEY,
    grade_value INTEGER NOT NULL,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    note TEXT,
    grade_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grade_student
        FOREIGN KEY (student_id)
        REFERENCES student (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_grade_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject (id)
        ON DELETE CASCADE
);