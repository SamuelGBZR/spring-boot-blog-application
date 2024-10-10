package com.example.spring_boot_blog_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
    private static final String[] WHITELIST = { "/register", "/posts/*", "/h2-console/*", "/" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITELIST)
            .permitAll()
            .anyRequest()
            .authenticated()
        );
        http.csrf(csrf -> csrf
            .disable()
        );
        http.headers(headers -> headers
            .frameOptions(frame -> frame
                .disable()
            )
        );
        http.formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error")
            .permitAll()
        );
        http.logout(logout -> logout
            .logoutUrl("/logout")
        );

        return http.build();
    }
}
