package com.example.authService.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authService.Dto.userLoginDto;
import com.example.authService.Entity.User;
import com.example.authService.Repository.UserRepo;
import com.example.authService.Service.UserService;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody userLoginDto userDto) {
        if(userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }
        return ResponseEntity.ok(userService.regsiterUser(userDto));
    }
    

}
    