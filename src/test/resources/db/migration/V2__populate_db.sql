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

INSERT INTO users (first_name, last_name, tel_number, email, passport_serial_number)
VALUES  ('denis', 'sidorov', '0965467834', 'sidor@gmail.com', ''),
        ('andriy', 'sidorov', '0954375647', 'sidor_andr@gmail.com', 'bb345678'),
        ('mark', 'dmitrenko', '0505463213', 'dmitr@gmail.com', 'va123456'),
        ('evgen', 'kozlov', '0964569034', 'kozlov@gmail.com', ''),
        ('andriy', 'nikolaenko', '0934560912', 'nikola@gmail.com', 'ba345863');

INSERT INTO reservation (room_id, check_in_date, check_out_date)
VALUES  (5, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 4 DAY)),
        (5, "2024-01-15", "2024-01-18"),
        (4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY)),
        (1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY));

INSERT INTO reservation_user (reservation_id, user_id)
VALUES  (1, 1),
        (1, 2),
        (2, 2),
        (3, 4),
        (3, 5),
        (4, 1),
        (4, 2),
        (4, 5);