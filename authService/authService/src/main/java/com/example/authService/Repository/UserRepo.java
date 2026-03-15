package com.example.authService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authService.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    // user repo methods can be defined here
}
