package com.robot.hotel.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Query("SELECT t FROM ConfirmationToken t LEFT JOIN FETCH t.user WHERE t.token = :token")
    Optional<ConfirmationToken> findByToken(String token);
}
