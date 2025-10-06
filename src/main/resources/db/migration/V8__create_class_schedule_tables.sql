CREATE TABLE IF NOT EXISTS class_schedule
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    class_schedule_name VARCHAR(255) NOT NULL,
    school_class_id BIGINT NOT NULL,
    CONSTRAINT fk_schedule_entry_school_class
            FOREIGN KEY (school_class_id)
            REFERENCES school_class (id)
);

CREATE TABLE IF NOT EXISTS schedule_entry
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    class_schedule_id BIGINT NOT NULL,
    day_of_week VARCHAR(30) NOT NULL,
    CONSTRAINT fk_schedule_entry_class_schedule
        FOREIGN KEY (class_schedule_id)
        REFERENCES class_schedule (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS time_slot
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    schedule_entry_id BIGINT NOT NULL,
    subject_id BIGINT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_break BOOLEAN NOT NULL,
    CONSTRAINT fk_time_slot_schedule_entry
        FOREIGN KEY (schedule_entry_id)
        REFERENCES schedule_entry (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_time_slot_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject (id)
);