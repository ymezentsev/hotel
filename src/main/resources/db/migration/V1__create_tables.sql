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

create table IF NOT EXISTS guest(
	id bigint primary key auto_increment,
    first_name varchar(50) not null,
	last_name varchar(50) not null,
	tel_number varchar(20) unique not null,
    email varchar(50) unique,
	passport_serial_number varchar(50) unique
);

create table IF NOT EXISTS reservation(
	id bigint primary key auto_increment,
    room_id bigint not null,
	check_in_date date not null,
	check_out_date date not null,
	foreign key (room_id) references room (id)
);

create table IF NOT EXISTS reservation_guest(
	reservation_id bigint not null,
    guest_id bigint not null,
	foreign key (reservation_id) references reservation (id),
    foreign key (guest_id) references guest (id)
);