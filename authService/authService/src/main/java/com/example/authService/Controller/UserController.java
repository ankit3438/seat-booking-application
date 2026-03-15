package com.example.authService.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authService.Dto.userLoginDto;
import com.example.authService.Entity.User;
import com.example.authService.Repository.UserRepo;
import com.example.authService.Security.JwtUtil;

import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * User Login - Generate JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody userLoginDto userDto) {
        try {
            if (userDto.getUsername() == null || userDto.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username and password are required");
            }

            // Authenticate user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDto.getUsername(),
                    userDto.getPassword()
                )
            );

            // Generate JWT token
            String token = jwtUtil.generateToken(userDto.getUsername());
            
            return ResponseEntity.ok("{\n" +
                "  \"token\": \"" + token + "\",\n" +
                "  \"type\": \"Bearer\",\n" +
                "  \"message\": \"Login successful\"\n" +
                "}");

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Login failed: " + e.getMessage());
        }
    }

    /**
     * User Registration
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody userLoginDto userDto) {
        try {
            if (userDto.getUsername() == null || userDto.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username and password are required");
            }

            // Check if user already exists
            if (userRepo.findByUsername(userDto.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole("USER");
            user.setLoginTime(LocalDateTime.now());
            user.setActive(true);

            userRepo.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Registration failed: " + e.getMessage());
        }
    }
}
    