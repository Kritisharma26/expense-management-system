package com.example.expnecemgmt.config;

import com.example.expnecemgmt.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Allow public access to authentication APIs
                .requestMatchers("/api/auth/**").permitAll()
                
                // ✅ Ensure signup works (already covered under /api/auth/**)
                .requestMatchers("/api/auth/signup").permitAll()
                
                // ✅ Dashboard data accessible by USER and ADMIN
                .requestMatchers("/api/expenses/dashboard-data").hasAnyRole("USER", "ADMIN")
                
                // ✅ Pending approvals are only for ADMIN
                .requestMatchers("/api/expenses/pending-approvals").hasRole("ADMIN")

                // ✅ Approval-related APIs are restricted to ADMIN (Check if this is intentional)
                .requestMatchers("/api/approvals/**").hasRole("ADMIN")

                // ✅ Ensure users & admins can create & view expenses
                .requestMatchers("/api/expenses").hasAnyRole("USER", "ADMIN") 
                .requestMatchers("/api/expenses/**").hasAnyRole("USER", "ADMIN") 

                // ✅ Ensure User Management APIs are accessible
                .requestMatchers("/api/users").hasRole("ADMIN") // Adjust based on your needs
                .requestMatchers("/api/users/**").hasRole("ADMIN") // Ensure admins can manage users
                
                // 🔒 Any other request requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
