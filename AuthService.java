package com.example.expnecemgmt.services;

import com.example.expnecemgmt.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public String authenticateUser(String identifier, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);
        return jwtUtil.generateToken(userDetails);
    }
}

