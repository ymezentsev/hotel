INSERT INTO users (first_name, last_name, phone_country_code, phone_number, email, password, passport_id, is_enabled)
values  ('admin', 'admin', 'UKR', '991111111', 'admin@gmail.com',
'$2a$12$owtTS8Q5teMgBiUMju1cy.NNDMGUhEKnelNJ8uL2Q/4FsvFg7/6Yq', null, true);
-- admin@gmail.com          |   password: Admin123

INSERT INTO roles (role_name)
VALUES ('USER'),
       ('MANAGER'),
       ('ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 3);