package com.task1.service.implementation;

import com.task1.builder.UserResponseDtoBuilder;
import com.task1.builder.UsersBuilder;
import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;
import com.task1.entity.Users;
import com.task1.service.UserService;
import com.task1.dao.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public userServiceImplementation(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto userCreate(UserRequestDto userRequestDto) {
        Users users = UsersBuilder.buildUsersFromUserRequestDto(userRequestDto);
        Users savedUser = userRepository.save(users);
        return UserResponseDtoBuilder.buildUserResponseDtoFromUsers(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        userRepository.findById(id).orElseThrow(new EntityNotFoundException("User not found"));
        return null;
    }
}
