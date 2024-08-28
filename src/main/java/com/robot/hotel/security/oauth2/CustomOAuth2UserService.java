package com.robot.hotel.security.oauth2;

import com.robot.hotel.user.model.Role;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.model.enums.RoleName;
import com.robot.hotel.user.repository.RoleRepository;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }

    public User registerNewUserAfterOAuthLogin(CustomOAuth2User oAuth2User) {
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
            return userRepository.save(user);
        }
        return existingUser.get();
    }
}