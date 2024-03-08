package com.robot.hotel.passport;

import com.robot.hotel.country.Country;
import com.robot.hotel.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Table (name = "passport")
@Entity
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

    @Column
    private LocalDate issueDate;

    @OneToOne(cascade = CascadeType.ALL, mappedBy="passport")
    private User user;
}
