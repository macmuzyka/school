CREATE TABLE previous_note
(
    id         BIGSERIAL PRIMARY KEY,
    content    TEXT NOT NULL,
    subject    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
