INSERT INTO rooms (id, room_number)
VALUES (1, '1310'),
       (2, '1511'),
       (3, '2101'),
       (4, '0715'),
       (5, '2018');

INSERT INTO users (id, email, hash_password, first_name, last_name, organization, role, state, confirmation,
                   confirm_code)
VALUES (100, 'admin@admin.com', '$2a$10$c2Tz57fcGKETTnjqNNsuEOMZFy9PXxbjlEhZVM4LvhV0b1BPhH5gm', 'Admin', 'Adminov',
        'Admin city', 'ADMIN', 'ACTIVE', 'CONFIRMED', '03122f33-0555-460e-a09d-9584e0df6795'),
       (101, 'speaker@speaker.com', '$2a$10$/4FUV1MFF4j7L9I80IJXG.qI227mnbACwreCF4zX4SGLYH6vSbVty', 'Speaker',
        'Speakerov', 'Speaker city', 'SPEAKER', 'ACTIVE', 'CONFIRMED', '07a2da3a-2e08-4fc8-9625-a4577f354ff3'),
       (102, 'someone@someone.com', '$2a$10$0PxIWRCrBy.Crlenodp8Aukwjp76pK.MjvHAlGqGWDqfCZwCLumHW', 'Someone',
        'Someoneov', 'Someone city', 'SPEAKER', 'ACTIVE', 'CONFIRMED', '7a0f7bf7-2a7a-4568-b6de-ed5173439a8f');

INSERT INTO talks (id, topic, description)
VALUES (100, 'Math', 'Algebra and logic'),
       (101, 'Physics', 'Quantum mechanics'),
       (102, 'Developments by a chemical laboratory', 'Development of reagents');

INSERT INTO talk_speakers (talk_id, speaker_id)
VALUES (100, 101),
       (100, 102),
       (101, 101),
       (102, 102);

INSERT INTO schedule (id, talk_time, room_id, talk_id)
VALUES (100, '16:00-17:00', 1, 100),
       (101, '12:00-14:30', 2, 101);
