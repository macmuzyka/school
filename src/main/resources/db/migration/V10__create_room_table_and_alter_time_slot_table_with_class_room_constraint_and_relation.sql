CREATE TABLE IF NOT EXISTS class_room
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    room_number INTEGER NOT NULL,
    occupied BOOLEAN DEFAULT FALSE,
    time_slot_id BIGINT DEFAULT NULL,
        CONSTRAINT fk_time_slot_class_room
            FOREIGN KEY (time_slot_id)
            REFERENCES time_slot (id)
);

ALTER TABLE IF EXISTS time_slot
ADD COLUMN class_room_id BIGINT DEFAULT NULL;

ALTER TABLE IF EXISTS time_slot
ADD CONSTRAINT fk_class_room_time_slot
    FOREIGN KEY (class_room_id)
    REFERENCES class_room (id);