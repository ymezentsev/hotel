package com.robot.hotel.user.country;

import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Table(name = "country")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    private String code;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String telCode;

    @OneToMany(mappedBy = "country")
    private List<Passport> passports;

    @OneToMany(mappedBy = "country")
    private List<User> users;
}