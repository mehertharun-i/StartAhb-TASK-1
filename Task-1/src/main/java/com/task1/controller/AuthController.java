package com.task1.controller;

import com.task1.dto.AuthResponse;
import com.task1.dto.LoginDetails;
import com.task1.dto.UserRequestDto;
import com.task1.security.JWTService;
import com.task1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, 
                         PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDetails loginDetails) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDetails.getUserLoginId(),
                    loginDetails.getPassword()
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, null, 0, null, "Invalid credentials"));
        }

        String accessToken = jwtService.generateToken(loginDetails.getUserLoginId());
        String refreshToken = jwtService.generateRefreshToken(loginDetails.getUserLoginId());

        AuthResponse response = new AuthResponse(
            accessToken,
            refreshToken,
            60000,
            "Bearer",
            "Login successful"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserRequestDto userRequestDto) {
        try {
            String encodedPassword = passwordEncoder.encode(userRequestDto.getUserPassword());
            userRequestDto.setUserPassword(encodedPassword);
            userService.userCreate(userRequestDto);

            String accessToken = jwtService.generateToken(userRequestDto.getUserLoginId());
            String refreshToken = jwtService.generateRefreshToken(userRequestDto.getUserLoginId());

            AuthResponse response = new AuthResponse(
                accessToken,
                refreshToken,
                60000,
                "Bearer",
                "Signup successful"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, null, 0, null, "Signup failed: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, 0, null, "Invalid token"));
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUserName(token);

            String newAccessToken = jwtService.generateToken(username);
            String newRefreshToken = jwtService.generateRefreshToken(username);

            AuthResponse response = new AuthResponse(
                newAccessToken,
                newRefreshToken,
                60000,
                "Bearer",
                "Token refreshed"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, null, 0, null, "Token refresh failed"));
        }
    }
}
