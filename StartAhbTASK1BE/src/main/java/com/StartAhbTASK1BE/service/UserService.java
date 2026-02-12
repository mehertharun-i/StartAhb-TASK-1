package com.task1.service;

import com.task1.dto.UpdateUserRequestDto;
import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    public UserResponseDto userCreate(UserRequestDto userRequestDto);

    public UserResponseDto getUserById(Long userIdd);

    public List<UserResponseDto> getAllUsers();

    public UserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto, Long userId);

    public Void deleteUser(Long userId);
}
