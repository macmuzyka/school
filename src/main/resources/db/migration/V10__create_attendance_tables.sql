CREATE TABLE IF NOT EXISTS attendance
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    attendance_timeframe_start TIME WITHOUT TIME ZONE NOT NULL,
    attendance_timeframe_end TIME WITHOUT TIME ZONE NOT NULL,
    attendance_date DATE NOT NULL,
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
    presence_status VARCHAR(30) NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_student_student_presence
        FOREIGN KEY (student_id)
        REFERENCES student (id),
    CONSTRAINT fk_attendance_student_presence
        FOREIGN KEY (attendance_id)
        REFERENCES attendance (id)
);