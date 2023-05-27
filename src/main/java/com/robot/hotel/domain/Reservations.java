package com.robot.hotel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate CheckOutDate;

    @ManyToMany()
    @JoinTable(
            name = "reservations_guests",
            joinColumns = @JoinColumn(name = "reservations_id"),
            inverseJoinColumns = @JoinColumn(name = "guests_id"))
    private Set<Guests> guests = new HashSet<>();
}
