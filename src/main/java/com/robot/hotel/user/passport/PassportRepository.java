package com.robot.hotel.user.passport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
    boolean existsBySerialNumber(String serialNumber);
}
