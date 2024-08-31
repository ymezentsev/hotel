package com.robot.hotel.security.oauth2;

import com.robot.hotel.user.model.Role;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.model.enums.RoleName;
import com.robot.hotel.user.repository.RoleRepository;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    //todo add test
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        log.info("User with email {} loaded successfully ",
                Objects.requireNonNull(user.getAttribute("email")).toString());
        return new CustomOAuth2User(user);
    }

    //todo add test
    public User registerNewUserAfterOAuthLogin(CustomOAuth2User oAuth2User) {
        log.info("Registering new user after OAuth login");
        Role roleUser = roleRepository.findByName(RoleName.USER).orElseThrow();

        Optional<User> existingUser = userRepository.findByEmail(oAuth2User.getEmail().toLowerCase());

        if (existingUser.isEmpty()) {
            User user = new User();
            user.setEmail(oAuth2User.getEmail());
            user.setFirstName(oAuth2User.getAttribute("given_name"));
            user.setLastName(oAuth2User.getAttribute("family_name"));
            user.setRoles(Collections.singleton(roleUser));
            if (Boolean.TRUE.equals(oAuth2User.getAttribute("email_verified"))) {
                user.setEnabled(true);
            }

            User newUser = userRepository.save(user);
            log.info("New user with email {} successfully registered", newUser.getEmail());
            return newUser;
        }
        log.info("User with email {} already exists", existingUser.get().getEmail());
        return existingUser.get();
    }
}