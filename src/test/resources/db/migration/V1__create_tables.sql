create table IF NOT EXISTS country(
	code varchar(3) primary key,
    name varchar(60) unique not null,
    tel_code varchar(5) not null
);

create table IF NOT EXISTS passport(
	id bigint primary key auto_increment,
    serial_number varchar(12) unique not null,
    country_code varchar(3) not null,
    issue_date date not null,
    foreign key (country_code) references country (code)
);

create table IF NOT EXISTS room_type(
	id bigint primary key auto_increment,
    type varchar(20) unique not null
);

create table IF NOT EXISTS room(
	id bigint primary key auto_increment,
    number varchar(20) unique not null,
    price decimal(10,2) not null,
    max_count_of_guests int not null,
    room_type_id bigint not null,
    foreign key (room_type_id) references room_type (id)
);

create table IF NOT EXISTS users(
	id bigint primary key auto_increment,
    first_name varchar(20) not null,
	last_name varchar(20) not null,
	tel_country_code varchar(3) not null,
	tel_number varchar(12) unique not null,
    email varchar(50) unique not null,
    password VARCHAR(100) not null,
    role VARCHAR(10) not null default 'USER',
	passport_id bigint,
	foreign key (passport_id) references passport (id),
	foreign key (tel_country_code) references country (code)
);

create table IF NOT EXISTS reservation(
	id bigint primary key auto_increment,
    room_id bigint not null,
	check_in_date date not null,
	check_out_date date not null,
	foreign key (room_id) references room (id)
);

create table IF NOT EXISTS reservation_user(
	reservation_id bigint not null,
    user_id bigint not null,
	foreign key (reservation_id) references reservation (id),
    foreign key (user_id) references users (id)
);