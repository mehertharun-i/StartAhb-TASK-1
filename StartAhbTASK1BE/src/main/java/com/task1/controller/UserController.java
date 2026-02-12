package com.task1.controller;

import com.task1.dto.LoginDetails;
import com.task1.dto.UpdateUserRequestDto;
import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;
import com.task1.security.JWTService;
import com.task1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    AuthenticationManager authenticationManager;

    JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> userCreate(@RequestBody UserRequestDto userRequestDto){
        String encodedPasword = passwordEncoder.encode(userRequestDto.getUserPassword());
        userRequestDto.setUserPassword(encodedPasword);
        UserResponseDto userResponseDto = userService.userCreate(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long userId){
        UserResponseDto userResponseDto = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> allUsers = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UpdateUserRequestDto updateUserRequestDto, @PathVariable("id") Long userId){
        UserResponseDto userResponseDto = userService.updateUser(updateUserRequestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDetails loginDetails){
        try{
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDetails.getUserLoginId(), loginDetails.getPassword()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Credentials"+e.getMessage());
        }
        String token = jwtService.generateToken(loginDetails.getUserLoginId());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
