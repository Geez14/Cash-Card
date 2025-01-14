package com.geez14.app.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests
                (
                        request -> request
                                .requestMatchers("/cashcards/**")
                                .hasRole("CARD-OWNER")
                ).httpBasic
                (
                        Customizer.withDefaults()
                ).csrf
                (
                        (CsrfConfigurer<HttpSecurity> csrf) -> csrf.disable()
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails mxtylish = users
                .username("Mxtylish")
                .password(passwordEncoder.encode("password1234"))
                .roles("CARD-OWNER")
                .build();
        UserDetails hawkThuWa = users.username("HackThuWa")
                .password(passwordEncoder.encode("password123456"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(mxtylish, hawkThuWa);
    }
}
