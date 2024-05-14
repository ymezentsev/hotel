package com.robot.hotel.user.passport;

import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Table (name = "passport")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    @Column (nullable = false)
    private LocalDate issueDate;

    @OneToOne(mappedBy="passport")
    private User user;
}