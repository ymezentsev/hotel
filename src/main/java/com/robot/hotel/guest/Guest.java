package com.robot.hotel.guest;

import com.robot.hotel.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guest")
@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String telNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String passportSerialNumber;

    @ManyToMany(mappedBy = "guests", fetch = FetchType.EAGER)
    private List<Reservation> reservations;
}
