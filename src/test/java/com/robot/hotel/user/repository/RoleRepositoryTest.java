package com.robot.hotel.user.repository;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.user.model.enums.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @ParameterizedTest
    @DisplayName("Successful find role by name")
    @ValueSource(strings = {"USER", "ADMIN", "MANAGER"})
    void findByRoleNameTest(String roleName) {
        assertTrue(roleRepository.findByName(RoleName.valueOf(roleName)).isPresent());
    }

    @Test
    @DisplayName("Fail find role by name")
    void findByRoleNameThrowsExceptionTest() {
        assertThrows(IllegalArgumentException.class,
                () -> roleRepository.findByName(RoleName.valueOf("NEW_USER")));
    }
}