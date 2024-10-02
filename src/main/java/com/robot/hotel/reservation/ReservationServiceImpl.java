package com.robot.hotel.reservation;

import com.robot.hotel.email.sevice.EmailReservationService;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.NoSuchElementException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
import com.robot.hotel.room.Room;
import com.robot.hotel.room.RoomMapper;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.room.RoomService;
import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.model.enums.RoleName;
import com.robot.hotel.user.repository.UserRepository;
import com.robot.hotel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final RoomService roomService;
    private final UserService userService;
    private final EmailReservationService emailReservationService;

    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;

    @Value("${max.duration.of.reservation.in.days}")
    private int maxDurationOfReservationInDays;
    @Value("${reservation.opening.time.in.days}")
    private int reservationOpeningTimeInDays;

    private static final String RESERVATION_IS_NOT_EXISTS = "Such reservation not exists";
    private static final String ROOM_IS_NOT_EXISTS = "Such room not exists";
    private static final String USER_IS_NOT_EXISTS = "Such user not exists";
    private static final String CHECK_OUT_LESS_THAN_CHECK_IN_DATE = "The check out date must be after check in date";
    private static final String TOO_LONG_RESERVATION = "You can't reserve room for more than %s days";
    private static final String TOO_EARLY_RESERVATION = "Reservation of rooms opens %s days in advance";
    private static final String TOO_MANY_GUESTS = "The quantity of guests exceeds the maximum allowed in this room";
    private static final String OCCUPIED_ROOM = "This room is occupied for your dates";
    private static final String SUCCESSFUL_ACTION_WITH_RESERVATION = "Successful %s reservation with id: {}";

    @Override
    public Page<ReservationDto> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public ReservationDto findById(Long id) {
        return reservationMapper.toDto(reservationRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(RESERVATION_IS_NOT_EXISTS)));
    }

    @Override
    public Page<ReservationDto> findReservationsByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findByUsersId(userId, pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> findReservationsByRoom(String roomNumber, Pageable pageable) {
        Long roomId = roomService.findByNumber(roomNumber).id();

        return reservationRepository.findByRoomId(roomId, pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public ReservationDto save(ReservationRequest reservationRequest) {
        log.info("Saving reservation with room number: {}", reservationRequest.getRoomNumber().toLowerCase().strip());

        Room room = roomRepository.findByNumber(reservationRequest.getRoomNumber().toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(ROOM_IS_NOT_EXISTS));

        if (reservationRequest.getCheckOutDate().isBefore(reservationRequest.getCheckInDate())
                || reservationRequest.getCheckOutDate().isEqual(reservationRequest.getCheckInDate())) {
            throw new WrongDatesException(CHECK_OUT_LESS_THAN_CHECK_IN_DATE);
        }

        if (ChronoUnit.DAYS.between(reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate())
                > maxDurationOfReservationInDays) {
            throw new WrongDatesException(String.format(TOO_LONG_RESERVATION, maxDurationOfReservationInDays));
        }

        if (ChronoUnit.DAYS.between(LocalDate.now(), reservationRequest.getCheckOutDate())
                > reservationOpeningTimeInDays) {
            throw new WrongDatesException(String.format(TOO_EARLY_RESERVATION, reservationOpeningTimeInDays));
        }

        if (reservationRequest.getUserEmails().size() > room.getMaxCountOfGuests()) {
            throw new GuestsQuantityException(TOO_MANY_GUESTS);
        }

        Set<User> users = reservationRequest.getUserEmails().stream()
                .map(email -> userRepository.findByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)))
                .collect(Collectors.toSet());

        List<RoomDto> freeRooms = roomService
                .findFreeRooms(new FreeRoomRequest(reservationRequest.getCheckInDate(),
                        reservationRequest.getCheckOutDate()));
        if (!freeRooms.contains(roomMapper.toDto(room))) {
            throw new WrongDatesException(OCCUPIED_ROOM);
        }

        Reservation newReservation = Reservation.builder()
                .room(room)
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .users(users)
                .build();

        Reservation savedReservation = reservationRepository.save(newReservation);
        log.info(String.format(SUCCESSFUL_ACTION_WITH_RESERVATION, "created"), savedReservation.getId());

        emailReservationService.sendReservationConfirmationEmail(savedReservation);
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public Page<ReservationDto> findCurrentReservations(Pageable pageable) {
        return reservationRepository.findCurrentReservations(pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public Page<ReservationDto> findCurrentReservationsForSpecificRoom(String roomNumber, Pageable pageable) {
        Long roomId = roomService.findByNumber(roomNumber).id();

        return reservationRepository.findCurrentReservationsForRoom(roomId, pageable)
                .map(reservationMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting reservation with id: {}", id);

        Optional<Reservation> reservationForDeleting = reservationRepository.findById(id);
        if (reservationForDeleting.isEmpty()) {
            throw new NoSuchElementException(RESERVATION_IS_NOT_EXISTS);
        }
        checkIfUserHasAuthorityToDeleteReservation(reservationForDeleting.get());

        reservationRepository.deleteById(id);
        emailReservationService.sendReservationCanceledEmail(reservationForDeleting.get());
        log.info(String.format(SUCCESSFUL_ACTION_WITH_RESERVATION, "deleted"), id);
    }

    private void checkIfUserHasAuthorityToDeleteReservation(Reservation reservation) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (!reservation.getUsers().contains(currentUser) &&
                currentUser.getRoles()
                        .stream()
                        .noneMatch(role ->
                                role.getName().equals(RoleName.ADMIN) || role.getName().equals(RoleName.MANAGER)
                        )) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}