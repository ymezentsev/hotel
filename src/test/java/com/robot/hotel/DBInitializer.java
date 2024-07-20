package com.robot.hotel;

import com.robot.hotel.country.CountryRepository;
import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.Room;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.Role;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.ConfirmationTokenRepository;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DBInitializer {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void populateDB() {
        reservationRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();
        populateRoomTypeTable();
        populateRoomTable();
        populateUserTable();
        populateConfirmationTokenTable();
        populateReservationTable();
    }

    private void populateRoomTypeTable() {
        roomTypeRepository.save(new RoomType(null, "lux", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart single", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart double", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "king", Collections.emptyList()));
    }

    private void populateRoomTable() {
        saveNewRoom("101", BigDecimal.valueOf(5000), 4, "lux");
        saveNewRoom("201", BigDecimal.valueOf(1500), 2, "standart single");
        saveNewRoom("202", BigDecimal.valueOf(1500), 2, "standart single");
        saveNewRoom("203", BigDecimal.valueOf(1000), 2, "standart double");
        saveNewRoom("204", BigDecimal.valueOf(1000), 2, "standart double");
    }

    private void saveNewRoom(String number, BigDecimal price, int maxCountOfGuests, String roomType) {
        roomRepository.save(Room.builder()
                .number(number)
                .price(price)
                .maxCountOfGuests(maxCountOfGuests)
                .roomType(roomTypeRepository.findByType(roomType).orElseThrow())
                .build());
    }

    private void populateUserTable() {
        Passport passport1 = saveNewPassport("bb345678", "UKR",
                LocalDate.of(2020, 1, 15));
        Passport passport2 = saveNewPassport("va123456", "UKR",
                LocalDate.of(2021, 3, 23));
        Passport passport3 = saveNewPassport("ba345863", "ITA",
                LocalDate.of(2021, 2, 12));

        saveNewUser("admin", "admin", "UKR",
                "991111111", "admin@gmail.com", "User1User1", Role.ADMIN, null);

        saveNewUser("denis", "sidorov", "UKR",
                "965467834", "sidor@gmail.com", "123", Role.USER, null);

        saveNewUser("andriy", "sidorov", "UKR",
                "954375647", "sidor_andr@gmail.com", "123", Role.USER, passport1);

        saveNewUser("mark", "dmitrenko", "UKR",
                "505463213", "dmitr@gmail.com", "123", Role.USER, passport2);

        saveNewUser("evgen", "kozlov", "UKR",
                "964569034", "kozlov@gmail.com", "123", Role.MANAGER, null);

        saveNewUser("andriy", "nikolaenko", "ITA",
                "0934560912", "nikola@gmail.com", "123", Role.USER, passport3);
    }

    private Passport saveNewPassport(String serialNumber, String country, LocalDate issueDate) {
        return Passport.builder()
                .serialNumber(serialNumber)
                .country(countryRepository.findById(country).orElseThrow())
                .issueDate(issueDate)
                .build();
    }

    private void saveNewUser(String firstName, String lastName,
                             String country, String phoneNumber,
                             String email, String password,
                             Role role, Passport passport) {
        userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .country(countryRepository.findById(country).orElseThrow())
                .phoneNumber(phoneNumber)
                .email(email)
                .password(password)
                .role(role)
                .passport(passport)
                .isEnabled(true)
                .build());
    }


    private void populateConfirmationTokenTable() {
        User user1 = userRepository.findByEmail("admin@gmail.com").orElseThrow();
        User user2 = userRepository.findByEmail("sidor@gmail.com").orElseThrow();
        User user5 = userRepository.findByEmail("kozlov@gmail.com").orElseThrow();

        confirmationTokenRepository.save(new ConfirmationToken(null,
                "ec410724-03b8-427a-a579-cbe965a543c7",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null,
                user1));

        confirmationTokenRepository.save(new ConfirmationToken(null,
                "6453fbfb-8ff9-4dea-b8c9-3c6807410cdb",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                LocalDateTime.now(),
                user2));

        confirmationTokenRepository.save(new ConfirmationToken(null,
                "6453fbfb-8ff9-4dea-b8c9-expired",
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().minusMinutes(15),
                null,
                user2));

        confirmationTokenRepository.save(new ConfirmationToken(null,
                "6453fbfb-8ff9-4dea-b8c9-notConfirmed",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null,
                user5));
    }

    private void populateReservationTable() {
        User user2 = userRepository.findByEmail("sidor@gmail.com").orElseThrow();
        User user3 = userRepository.findByEmail("sidor_andr@gmail.com").orElseThrow();
        User user5 = userRepository.findByEmail("kozlov@gmail.com").orElseThrow();
        User user6 = userRepository.findByEmail("nikola@gmail.com").orElseThrow();

        saveNewReservation("204", LocalDate.now(),
                LocalDate.now().plusDays(4), Set.of(user2, user3));
        saveNewReservation("204", LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 18), Set.of(user3));
        saveNewReservation("203", LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(6), Set.of(user5, user6));
        saveNewReservation("101", LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(3), Set.of(user2, user3, user6));
    }

    private void saveNewReservation(String number, LocalDate checkInDate, LocalDate checkOutDate, Set<User> users) {
        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber(number).orElseThrow())
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .users(users)
                .build());
    }
}