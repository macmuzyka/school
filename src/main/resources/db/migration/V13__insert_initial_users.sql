INSERT INTO app_user (username, email, password) VALUES
('admin', 'example@email.com', '$2a$10$iFhi9Aan0H0dIUvs3WdO5.RYqkyyI5xPg7SKbH9CKMP93wL7uP8cq'),
('user',  'example2@email.com','$2a$10$awvNv5Btm3VVeiursZGovetAp.rPA9rvmOAR2nFo8uIYVa97q4Quq');

INSERT INTO app_user_role (app_user_id, roles) VALUES
(1, 'ADMIN'),
(2, 'USER');
