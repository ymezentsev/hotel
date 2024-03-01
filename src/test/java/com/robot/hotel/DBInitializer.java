package com.robot.hotel;

import com.robot.hotel.guest.Guest;
import com.robot.hotel.guest.GuestRepository;
import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.Room;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
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
    GuestRepository guestRepository;

    public void populateDB() {
        reservationRepository.deleteAll();
        guestRepository.deleteAll();
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();
        populateRoomTypeTable();
        populateRoomTable();
        populateGuestTable();
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

    private void populateGuestTable() {
        guestRepository.save(Guest.builder()
                .firstName("denis")
                .lastName("sidorov")
                .telNumber("0965467834")
                .email("sidor@gmail.com")
                .build());

        guestRepository.save(Guest.builder()
                .firstName("andriy")
                .lastName("sidorov")
                .telNumber("0954375647")
                .email("sidor_andr@gmail.com")
                .passportSerialNumber("bb345678")
                .build());

        guestRepository.save(Guest.builder()
                .firstName("mark")
                .lastName("dmitrenko")
                .telNumber("0505463213")
                .email("dmitr@gmail.com")
                .passportSerialNumber("va123456")
                .build());

        guestRepository.save(Guest.builder()
                .firstName("evgen")
                .lastName("kozlov")
                .telNumber("0964569034")
                .email("kozlov@gmail.com")
                .build());

        guestRepository.save(Guest.builder()
                .firstName("andriy")
                .lastName("nikolaenko")
                .telNumber("0934560912")
                .email("nikola@gmail.com")
                .passportSerialNumber("ba345863")
                .build());
    }

    private void populateReservationTable() {
        Guest guest1 = guestRepository.findByEmail("sidor@gmail.com").orElseThrow();
        Guest guest2 = guestRepository.findByEmail("sidor_andr@gmail.com").orElseThrow();
        Guest guest4 = guestRepository.findByEmail("kozlov@gmail.com").orElseThrow();
        Guest guest5 = guestRepository.findByEmail("nikola@gmail.com").orElseThrow();

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("204").orElseThrow())
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now().plusDays(4))
                .guests(List.of(guest1, guest2))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("204").orElseThrow())
                .checkInDate(LocalDate.of(2024, 1, 15))
                .checkOutDate(LocalDate.of(2024, 1, 18))
                .guests(List.of(guest2))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("203").orElseThrow())
                .checkInDate(LocalDate.now().plusDays(4))
                .checkOutDate(LocalDate.now().plusDays(6))
                .guests(List.of(guest4, guest5))
                .build());

        reservationRepository.save(Reservation.builder()
                .room(roomRepository.findByNumber("101").orElseThrow())
                .checkInDate(LocalDate.now().minusDays(2))
                .checkOutDate(LocalDate.now().plusDays(3))
                .guests(List.of(guest1, guest2, guest5))
                .build());
    }
}
