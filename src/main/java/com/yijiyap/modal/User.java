package com.yijiyap.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yijiyap.domain.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;


@Entity
@Data
@Table(name= "users")
public class User {

// Instance variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    private String email;

    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Default role.
    private UserRole role = UserRole.ROLE_CUSTOMER;

    // For 2FA Auth.
    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    // Default constructor for JPA to use.
    public User() {}

    // To create instances of User to be saved to the database.
    public User(String fullName, String email, String password) {
        if (fullName == null || fullName.trim().isEmpty() || email == null || password == null) {
            throw new IllegalArgumentException("fullName or email or password cannot be null");
        }
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = UserRole.ROLE_CUSTOMER;
        this.twoFactorAuth = new TwoFactorAuth();
    }
}
