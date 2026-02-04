package com.task1.service;

import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;

public interface UserService {

    public UserResponseDto userCreate(UserRequestDto userRequestDto);

    public UserResponseDto getUserById(Long id);
}
