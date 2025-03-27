package com.example.expnecemgmt.controllers;

import com.example.expnecemgmt.models.User;
import com.example.expnecemgmt.repositories.UserRepository;
import com.example.expnecemgmt.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserDetailsService userDetailsService, UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email"); // Ensure frontend sends "email"
        String password = loginRequest.get("password");

        System.out.println("🔍 Login attempt: Email = " + email + ", Password = " + password);

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing email or password"));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (BadCredentialsException e) {
            System.err.println("❌ Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Something went wrong"));
        }
    }

    @PostMapping("/signup")
public ResponseEntity<?> signupUser(@RequestBody Map<String, String> signupRequest) {
    String email = signupRequest.get("email");
    String username = signupRequest.get("username"); // ✅ Added
    String password = signupRequest.get("password");
    String role = signupRequest.get("role"); // Expecting "USER" or "ADMIN"

    if (email == null || password == null || role == null || username == null) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing email, username, password, or role"));
    }

    // Check if user already exists
    if (userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", "User already exists"));
    }

    // Create and save user
    User newUser = new User();
    newUser.setEmail(email);
    newUser.setUsername(username); // ✅ Set username
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setRole(role.equalsIgnoreCase("ADMIN") ? "ROLE_ADMIN" : "ROLE_USER");

    userRepository.save(newUser);

    return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
}
}
