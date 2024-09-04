package com.robot.hotel.security.oauth2;

import com.robot.hotel.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final CustomOAuth2UserService customOAuth2UserService;
    // private final JwtUtil jwtUtil;
/*    @Value("${frontend.base.url}")
    String frontendBaseUrl;*/

    @Override
    //todo add frontend page to receive token
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User loggedInUser = customOAuth2UserService.registerNewUserAfterOAuthLogin(oAuth2User);

        log.info("Google attributes of the logged in user: {}", oAuth2User.getAttributes().toString());

        //String token = jwtUtil.generateToken(loggedInUser.getEmail());
        //response.sendRedirect(frontendBaseUrl + "?token=" + token);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}