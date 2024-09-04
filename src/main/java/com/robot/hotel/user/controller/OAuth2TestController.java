package com.robot.hotel.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/google")
@RequiredArgsConstructor
public class OAuth2TestController {

    @GetMapping()
    public ModelAndView register() {
       return new ModelAndView("google-login");
    }
}
