CREATE TABLE IF NOT EXISTS country (
	country_code VARCHAR(3) PRIMARY KEY,
    country_name VARCHAR(60) UNIQUE NOT NULL,
    phone_code VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS passport (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    serial_number VARCHAR(12) UNIQUE NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    issue_date DATE NOT NULL,
    FOREIGN KEY (country_code) REFERENCES country (country_code)
);

CREATE TABLE IF NOT EXISTS room_type (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS room (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    number VARCHAR(20) UNIQUE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    max_count_of_guests INT NOT NULL,
    room_type_id BIGINT NOT NULL,
    FOREIGN KEY (room_type_id) REFERENCES room_type (id)
);

CREATE TABLE IF NOT EXISTS users (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	phone_country_code VARCHAR(3) NOT NULL,
	phone_number VARCHAR(12) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
	passport_id BIGINT,
	is_enabled BOOLEAN NOT NULL,
	FOREIGN KEY (passport_id) REFERENCES passport (id),
	FOREIGN KEY (phone_country_code) REFERENCES country (country_code)
);

CREATE TABLE IF NOT EXISTS reservation (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id bigint NOT NULL,
	check_in_date DATE NOT NULL,
	check_out_date DATE NOT NULL,
	FOREIGN KEY (room_id) REFERENCES room (id)
);

CREATE TABLE IF NOT EXISTS reservation_user (
	reservation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
	FOREIGN KEY (reservation_id) REFERENCES reservation (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS confirmation_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS forgot_password_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);