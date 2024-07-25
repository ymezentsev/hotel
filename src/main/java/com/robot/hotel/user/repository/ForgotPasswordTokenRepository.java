package com.robot.hotel.user.repository;

import com.robot.hotel.user.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    @Query("SELECT t FROM ForgotPasswordToken t LEFT JOIN FETCH t.user WHERE t.token = :token")
    Optional<ForgotPasswordToken> findByToken(String token);
}
