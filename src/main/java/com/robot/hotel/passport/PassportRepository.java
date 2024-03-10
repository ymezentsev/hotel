package com.robot.hotel.passport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
    //Optional<Passport> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
}
