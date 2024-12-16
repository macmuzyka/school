CREATE TABLE previous_note
(
    id         SERIAL PRIMARY KEY,
    content    TEXT NOT NULL,
    subject    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
