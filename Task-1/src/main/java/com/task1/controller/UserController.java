package com.task1.controller;

import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;
import com.task1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> userCreate(@RequestBody UserRequestDto userRequestDto){
        UserResponseDto userResponseDto = userService.userCreate(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id){
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public


}
