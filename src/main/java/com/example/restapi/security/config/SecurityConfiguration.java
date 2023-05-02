package com.example.restapi.security.config;

import com.example.restapi.security.jwt.JwtTokenProvider;
import com.example.restapi.security.repositories.UserDetailsRepository;
import com.example.restapi.security.jwt.JwtTokenAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenProvider tokenProvider;

    public SecurityConfiguration(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(auth ->
                        auth
//                                .requestMatchers("/users**").authenticated()
                                .anyRequest().permitAll()
                ).addFilterBefore(new JwtTokenAuthFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable()
                .and()
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(UserDetailsRepository repository) {
        return (username) ->
                repository.findById(username)
                        .orElseThrow(() -> new RuntimeException("Username: " + username + " not found"));

    }

    @Bean
    AuthenticationManager customManager(PasswordEncoder encoder, UserDetailsService service) {
        return (authentication) -> {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserDetails userDetails = service.loadUserByUsername(username);
            if (!encoder.matches(password, userDetails.getPassword())) {
                throw new RuntimeException("Bad password " + password);
            }

            return new UsernamePasswordAuthenticationToken(username, "", userDetails.getAuthorities());
        };
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
