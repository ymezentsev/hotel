package com.robot.hotel.room;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.roomtype.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String number;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int maxCountOfGuests;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}