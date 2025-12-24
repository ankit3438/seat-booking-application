package com.example.authService.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.authService.Dto.userLoginDto;
import com.example.authService.Entity.User;
import com.example.authService.Repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public String regsiterUser(userLoginDto userDto) {
        // Implement user registration logic here
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setActive(true);
        user.setRole("user");
        user.setLoginTime(LocalDateTime.now());
        try{
            userRepo.save(user);
        } catch(Exception e){
            return e.getMessage();
        }
        return "User registered successfully";
    }
}
