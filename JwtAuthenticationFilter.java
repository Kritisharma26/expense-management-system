package com.example.expnecemgmt.security;

import com.example.expnecemgmt.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userService;
    }

     @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        System.out.println("❌ No JWT Token Found in Request");
        filterChain.doFilter(request, response);
        return;
    }

    String token = authorizationHeader.substring(7);
    String username;
    
    try {
        username = jwtUtil.extractUsername(token);
        System.out.println("🔍 Extracted Username: " + username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
            System.out.println("✅ Authenticated User: " + userDetails.getUsername());
            System.out.println("✅ User Roles: " + userDetails.getAuthorities());
        } else {
            System.out.println("❌ Invalid Token");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT Token");
            return;
        }
    } catch (Exception e) {
        System.out.println("❌ JWT Error: " + e.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT Error: " + e.getMessage());
        return;
    }

    filterChain.doFilter(request, response);
}

}
