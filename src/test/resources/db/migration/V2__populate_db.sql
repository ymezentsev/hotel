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


INSERT INTO reservation (room_id, check_in_date, check_out_date)
VALUES  (5, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 4 DAY));