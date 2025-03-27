package com.example.expnecemgmt.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;  // Ensure email is added
    private String password;
    
    @Column(nullable = false)
    private String role; // ✅ Added role field

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // ✅ Constructor including role
    public User(String username, String email, String password, String role, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.roles = roles;
    }

    public User() {}

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getRole() { return role; } // ✅ Getter for role

    public Set<Role> getRoles() { return roles; }

    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setRole(String role) { this.role = role; } // ✅ Setter for role

    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
