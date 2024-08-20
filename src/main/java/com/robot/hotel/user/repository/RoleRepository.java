package com.robot.hotel.user.repository;

import com.robot.hotel.user.model.Role;
import com.robot.hotel.user.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //todo add tests
    Optional<Role> findByName(RoleName role);
}
