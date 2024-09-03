package com.robot.hotel.country;

import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Table(name = "country")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"passports", "users"})
public class Country {
    @Id
    private String countryCode;

    @Column(unique = true, nullable = false)
    private String countryName;

    @Column(unique = true, nullable = false)
    private String phoneCode;

    @OneToMany(mappedBy = "country")
    private List<Passport> passports;

    @OneToMany(mappedBy = "country")
    private List<User> users;
}