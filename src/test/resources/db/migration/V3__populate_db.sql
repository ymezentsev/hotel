INSERT INTO room_type (type)
VALUES  ('lux'),
		('standart single'),
		('standart double'),
		('king');

INSERT INTO room (number, price, max_count_of_guests, room_type_id)
VALUES  ('101', 5000, 4, 1),
        ('201', 1500, 2, 2),
        ('202', 1500, 2, 2),
        ('203', 1000, 2, 3),
        ('204', 1000, 2, 3);

INSERT INTO passport (serial_number, country_code, issue_date)
VALUES  ('bb345678', 'UKR', "2020-01-15"),
        ('va123456', 'UKR', "2021-03-23"),
        ('ba345863', 'ITA', "2021-02-12");

INSERT INTO users (first_name, last_name, phone_country_code, phone_number, email, password, role, passport_id)
VALUES  ('admin', 'admin', 'UKR', '991111111', 'admin@gmail.com', 'User1User1', 'ADMIN', NULL),
        ('denis', 'sidorov', 'UKR', '965467834', 'sidor@gmail.com', '123', 'USER', NULL),
        ('andriy', 'sidorov', 'UKR', '954375647', 'sidor_andr@gmail.com', '123', 'USER', 1),
        ('mark', 'dmitrenko', 'UKR', '505463213', 'dmitr@gmail.com', '123', 'USER', 2),
        ('evgen', 'kozlov', 'UKR', '964569034', 'kozlov@gmail.com', '123', 'MANAGER', NULL),
        ('andriy', 'nikolaenko', 'ITA', '0934560912', 'nikola@gmail.com', '123', 'USER', 3);

INSERT INTO reservation (room_id, check_in_date, check_out_date)
VALUES  (5, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 4 DAY)),
        (5, "2024-01-15", "2024-01-18"),
        (4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY)),
        (1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY));

INSERT INTO reservation_user (reservation_id, user_id)
VALUES  (1, 2),
        (1, 3),
        (2, 3),
        (3, 5),
        (3, 6),
        (4, 2),
        (4, 3),
        (4, 6);