package com.robot.hotel.user;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.passport.Passport;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity()
@EqualsAndHashCode(exclude = {"country", "passport", "reservations"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "phone_country_code", nullable = false)
    private Country country;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @ManyToMany(mappedBy = "users")
    private Set<Reservation> reservations;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = false;
}