package com.robot.hotel.config;

import com.robot.hotel.security.AuthEntryPointJwt;
import com.robot.hotel.security.JwtAuthenticationFilter;
import com.robot.hotel.security.oauth2.CustomOAuth2UserService;
import com.robot.hotel.security.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/v1/auth/**",
                                        "/api/v1/users/forgot-password/**",
                                        "/api/v1/users/reset-password/**",
                                        "/api/v1/google/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/rooms/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(infoEndpoint -> infoEndpoint.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .authorizationEndpoint(authEndpoint ->
                                authEndpoint.baseUri("/api/v1/login/oauth2/authorization"))
                        .redirectionEndpoint(redEndpoint ->
                                redEndpoint.baseUri("/api/v1/login/oauth2/code/*")))
                .logout(logout -> logout.clearAuthentication(true)
                        .logoutUrl("/logout"))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}