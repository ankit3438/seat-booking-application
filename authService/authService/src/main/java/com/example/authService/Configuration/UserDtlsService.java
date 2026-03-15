package com.example.authService.Configuration;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.authService.Entity.User;
import com.example.authService.Repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDtlsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement user details loading logic here
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRole())
                    .build();
        }
        else throw new UsernameNotFoundException("User not found with username: " + username);
    }

}                                                                                                                                                                                         