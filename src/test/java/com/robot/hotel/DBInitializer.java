package com.robot.hotel;

import com.robot.hotel.user.country.CountryRepository;
import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.Room;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.user.Role;
import com.robot.hotel.user.User;
import com.robot.hotel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class DBInitializer {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CountryRepository countryRepository;

    public void populateDB() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();
        populateRoomTypeTable();
        populateRoomTable();
        populateUserTable();
        populateReservationTable();
    }

    private void populateRoomTypeTable() {
        roomTypeRepository.save(new RoomType(null, "lux", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart single", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart double", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "king", Collections.emptyList()));
    }

    private void populateRoomTable() {
        roomRepository.save(Room.builder()
                .number("101")
                .price(BigDecimal.valueOf(5000))
                .maxCountOfGuests(4)
                .roomType(roomTypeRepository.findByType("lux").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("201")
                .price(BigDecimal.valueOf(1500))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart single").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("202")
                .price(BigDecimal.valueOf(1500))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart single").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("203")
                .price(BigDecimal.valueOf(1000))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart double").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("204")
                .price(BigDecimal.valueOf(1000))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart double").orElseThrow())
                .build());
    }

    private void populateUserTable() {
        Passport passport1 = Passport.builder()
                .serialNumber("bb345678")
                .country(countryRepository.findById("UKR").orElseThrow())
                .issueDate(LocalDate.of(2020, 1, 15))
                .build();

        Passport passport2 = Passport.builder()
                .serialNumber("va123456")
                .country(countryRepository.findById("UKR").orElseThrow())
                .issueDate(LocalDate.of(2021, 3, 23))
                .build();

        Passport passport3 = Passport.builder()
                .serialNumber("ba345863")
                .country(countryRepository.findById("ITA").orElseThrow())
                .issueDate(LocalDate.of(2021, 2, 12))
                .build();

        userRepository.save(User.builder()
                .firstName("admin")
                .lastName("admin")
                .country(countryRepository.findById("UKR").orElseThrow())
                .phoneNumber("991111111")
                .email("admin@gmail.com")
                .password("User1User1")
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .firstName("denis")
                .lastName("sidorov")
                .country(countryRepository.findById("UKR").orElseThrow())
                .phoneNumber("965467834")
                .email("sidor@gmail.com")
                .password("123")
                .role(Role.USER)
                .build());

        userRepository.save(User.builder()
                .firstName("andriy")
                .lastName("sidorov")
                .country(countryRepository.findById("UKR").orElseThrow())
                .phoneNumber("954375647")
                .email("sidor_andr@gmail.com")
                .password("123")
                .role(Role.USER)
                .passport(passport1)
                .build());

        userRepository.save(User.builder()
                .firstName("mark")
                .lastName("dmitrenko")
                .country(countryRepository.findById("UKR").orElseThrow())
                .phoneNumber("505463213")
                .email("dmitr@gmail.com")
                .password("123")
                .role(Role.USER)
                .passport(passport2)
                .build());

        userRepository.save(User.builder()
                .firstName("evgen")
                .lastName("kozlov")
                .country(countryRepository.findById("UKR").orElseThrow())
                .phoneNumber("964569034")
                .email("kozlov@gmail.com")
                .password("123")
                .role(Role.MANAGER)
                .build());

        userRepository.save(User.builder()
                .firstName("andriy")
                .lastName("nikolaenko")
                .country(countryRepository.findById("ITA").orElseThrow())
                .phoneNumber("0934560912")
                .email("nikola@gmail.com")
                .password("123")
                .role(Role.USER)
                .passport(passport3)
                .build());
    }

    private void populateReservationTable() {
        User user2 = userRepository.findByEmail("sidor@gmail.com").orElseThrow();
        User user3 = userRepository.findByEmail("sidor_andr@gmail.com").orElseThrow();
        User user5 = userRepository.findByEmail("kozlov@gmail.com").orElseThrow();
        User user6 = userRepository.findByEmail("nikola@gmail.com").orElseThrow();

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("204").orElseThrow())
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(4))
                .users(List.of(user2, user3))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("204").orElseThrow())
                .checkInDate(LocalDate.of(2024, 1, 15))
                .checkOutDate(LocalDate.of(2024, 1, 18))
                .users(List.of(user3))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("203").orElseThrow())
                .checkInDate(LocalDate.now().plusDays(4))
                .checkOutDate(LocalDate.now().plusDays(6))
                .users(List.of(user5, user6))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("101").orElseThrow())
                .checkInDate(LocalDate.now().minusDays(2))
                .checkOutDate(LocalDate.now().plusDays(3))
                .users(List.of(user2, user3, user6))
                .build());
    }
}