package com.task1.service.implementation;

import com.task1.builder.ListOfUserResponseDtoBuilder;
import com.task1.builder.UserResponseDtoBuilder;
import com.task1.builder.UsersBuilder;
import com.task1.dto.UpdateUserRequestDto;
import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;
import com.task1.entity.Users;
import com.task1.service.UserService;
import com.task1.dao.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public UserResponseDto getUserById(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserResponseDtoBuilder.buildUserResponseDtoFromUsers(users);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<Users> allUsers = userRepository.findAll();
        return ListOfUserResponseDtoBuilder.buildListOfUserResponseDtoFromUsers(allUsers);
    }

    @Override
    public UserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto, Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Users updatedUsers = UsersBuilder.buildUsersFromUserRequestDto(updateUserRequestDto, users);
        Users updatedUserSaved = userRepository.save(updatedUsers);
        return UserResponseDtoBuilder.buildUserResponseDtoFromUsers(updatedUserSaved);
    }

    @Override
    public Void deleteUser(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.deleteById(users.getUserId());
        return null;
    }


}
