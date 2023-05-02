package com.example.restapi.controllers;

import com.example.restapi.security.AuthRequest;
import com.example.restapi.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public TokenController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping({"/token", "/token/"})
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody AuthRequest authRequest) {
        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new RuntimeException("Username/Password can't be empty!");
        }
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", auth.getPrincipal());
        map.put("token", tokenProvider.createToken(auth));

        return ResponseEntity.ok(map);
    }

}
