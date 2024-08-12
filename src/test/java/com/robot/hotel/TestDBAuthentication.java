package com.robot.hotel;

import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestDBAuthentication {
    private final AuthenticationService authenticationService;

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public void loginUser() {
        //  if (Objects.isNull(token.get())) {
            token.set(authenticationService
                    .authenticate(new LoginRequestDto("sidor_andr@gmail.com", "Qwerty123456"))
                    .token());
      //  }
    }

    public String getToken() {
        return token.get();
    }
}
