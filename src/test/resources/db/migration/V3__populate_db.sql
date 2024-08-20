insert into room_type (type)
values  ('lux'),
		('standart single'),
		('standart double'),
		('king');

insert into room (number, price, max_count_of_guests, room_type_id)
values  ('101', 5000, 4, 1),
        ('201', 1500, 2, 2),
        ('202', 1500, 2, 2),
        ('203', 1000, 2, 3),
        ('204', 1000, 2, 3);

insert into passport (serial_number, country_code, issue_date)
values  ('bb345678', 'UKR', "2020-01-15"),
        ('va123456', 'UKR', "2021-03-23"),
        ('ba345863', 'ITA', "2021-02-12");

insert into users (first_name, last_name, phone_country_code, phone_number, email, password, passport_id, is_enabled)
values  ('admin', 'admin', 'UKR', '991111111', 'admin@gmail.com',
'$2a$12$owtTS8Q5teMgBiUMju1cy.NNDMGUhEKnelNJ8uL2Q/4FsvFg7/6Yq', null, true),
        ('denis', 'sidorov', 'UKR', '965467834', 'sidor@gmail.com',
'$2a$10$3ihcnQMi14khuO1wKxc3v.IwION2eN4L2bpwl6Udm/ghoZ74pqqmO', null, false),
        ('andriy', 'sidorov', 'UKR', '954375647', 'sidor_andr@gmail.com',
'$2a$10$3ihcnQMi14khuO1wKxc3v.IwION2eN4L2bpwl6Udm/ghoZ74pqqmO',1, true),
        ('mark', 'dmitrenko', 'UKR', '505463213', 'dmitr@gmail.com',
'$2a$10$3ihcnQMi14khuO1wKxc3v.IwION2eN4L2bpwl6Udm/ghoZ74pqqmO', 2, true),
        ('evgen', 'kozlov', 'UKR', '964569034', 'kozlov@gmail.com',
'$2a$10$3ihcnQMi14khuO1wKxc3v.IwION2eN4L2bpwl6Udm/ghoZ74pqqmO', null, true),
        ('andriy', 'nikolaenko', 'ITA', '0934560912', 'nikola@gmail.com',
'$2a$10$3ihcnQMi14khuO1wKxc3v.IwION2eN4L2bpwl6Udm/ghoZ74pqqmO', 3, false);
-- admin@gmail.com          |   password: Admin123
-- other users              |   password: Qwerty123456

INSERT INTO roles (role_name)
VALUES ('USER'),
       ('MANAGER'),
       ('ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 3),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 2),
       (6, 1);

INSERT INTO reservation (room_id, check_in_date, check_out_date)
VALUES  (5, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 4 DAY)),
        (5, "2024-01-15", "2024-01-18"),
        (4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY)),
        (1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY));

insert into reservation_user (reservation_id, user_id)
values  (1, 2),
        (1, 3),
        (2, 3),
        (3, 5),
        (3, 6),
        (4, 2),
        (4, 3),
        (4, 6);

insert into confirmation_token (token, created_at, expires_at, confirmed_at, user_id)
values ('ec410724-03b8-427a-a579-cbe965a543c7', NOW(), NOW() + interval 15 minute, null, 1),
('6453fbfb-8ff9-4dea-b8c9-3c6807410cdb', NOW(), NOW() + interval 15 minute, NOW(), 2),
('6453fbfb-8ff9-4dea-b8c9-expired', NOW() - interval 30 minute, NOW() - interval 15 minute, null, 2),
('6453fbfb-8ff9-4dea-b8c9-notConfirmed', NOW(), NOW() + interval 15 minute, null, 6);

insert into forgot_password_token (token, created_at, expires_at, confirmed_at, user_id)
values ('51b1ec6c-2a57-4b42-b9f5-7efc5cc4a0f6', NOW(), NOW() + interval 15 minute, NOW(), 1),
('8ac319b4-990f-466f-8a5a-7c2a028b430c', NOW(), NOW() + interval 15 minute, null, 2),
('8ac319b4-990f-466f-8a5a-expired', NOW() - interval 30 minute, NOW() - interval 15 minute, null, 2);