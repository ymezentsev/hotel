package com.robot.hotel.search_criteria.user;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.user.User;
import com.robot.hotel.user.dto.UserSearchParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserSpecificationBuilderTest {
    @Autowired
    UserSpecificationBuilder userSpecificationBuilder;

    @Test
    @DisplayName("Successful build specification")
    void buildTest() {
        Specification<User> originalSpec = Specification.where(null);

        UserSearchParameters searchParameters = new UserSearchParameters(
                new String[]{"Dmitro"},
                new String[]{"Andreev"},
                new String[]{"+380503456737"},
                new String[]{"andr@gmail.com"},
                new String[]{"USER"},
                new String[]{"BB012345"},
                new String[]{"1"},
                new String[]{"UKR"});
        assertAll(
                () -> assertEquals(originalSpec, userSpecificationBuilder.build((UserSearchParameters) null)),
                () -> assertNotEquals(originalSpec, userSpecificationBuilder.build(searchParameters))
        );
    }
}