package com.nvrsocial.market.config;

import com.nvrsocial.market.dto.response.UserResponse;
import com.nvrsocial.market.exception.RestAuthenticationFailureHandler;
import com.nvrsocial.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    public SecurityConfig(ObjectMapper objectMapper, UserRepository userRepository, RestAuthenticationFailureHandler restAuthenticationFailureHandler) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin(form -> form.loginProcessingUrl("/api/auth/login")
                        .successHandler((request, response, authentication) -> {
                            var user = userRepository.findByUsername(authentication.getName()).orElseThrow();
                            response.setStatus(200);
                            response.setContentType("application/json");
                            objectMapper.writeValue(response.getOutputStream(), new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
                        })
                        .failureHandler(restAuthenticationFailureHandler)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(204))
                        .permitAll()
                );

        return http.build();
    }
}
