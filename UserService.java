package com.example.expnecemgmt.services;

import com.example.expnecemgmt.models.User;
import com.example.expnecemgmt.repositories.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(identifier)
            .or(() -> userRepository.findByEmail(identifier)) // Fetch by email if username is not found
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier));

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(), // OR user.getEmail(), based on how you want to use it
            user.getPassword(),
            user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName())) // Ensure correct role format
                    .toList()
    );
}

}
