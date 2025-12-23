package com.example.authService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authService.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // user repo methods can be defined here
}
