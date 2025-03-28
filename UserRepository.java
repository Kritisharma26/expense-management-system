package com.example.expnecemgmt.repositories;

import com.example.expnecemgmt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // ✅ Find user by either email or username
    Optional<User> findByUsernameOrEmail(String username, String email);
}
