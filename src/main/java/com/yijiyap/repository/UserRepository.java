package com.yijiyap.repository;

import com.yijiyap.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find a user by email
     User findByEmail(String email);
}
