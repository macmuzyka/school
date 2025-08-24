CREATE TABLE IF NOT EXISTS attendance
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    presence_timeframe_start DATE NOT NULL,
    presence_timeframe_end DATE NOT NULL,
    subject_id BIGINT NOT NULL,
    time_slot_id BIGINT NOT NULL,
        CONSTRAINT fk_subject_attendance
        FOREIGN KEY (subject_id)
        REFERENCES subject (id),
        CONSTRAINT fk_time_slot_attendance
        FOREIGN KEY (time_slot_id)
        REFERENCES time_slot (id)
);

CREATE TABLE IF NOT EXISTS student_presence
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    student_id BIGINT NOT NULL,
    attendance_id BIGINT NOT NULL,
    present BOOLEAN NOT NULL,
    note VARCHAR(255),
        CONSTRAINT fk_student_student_presence
        FOREIGN KEY (student_id)
        REFERENCES student (id),
        CONSTRAINT fk_attendance_student_presence
        FOREIGN KEY (attendance_id)
        REFERENCES attendance (id)
);