package com.task1.builder;

import com.task1.dto.UserResponseDto;
import com.task1.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class ListOfUserResponseDtoBuilder {

    public static List<UserResponseDto> buildListOfUserResponseDtoFromUsers(List<Users> allUsers) {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for(Users user : allUsers) {
            UserResponseDto userResponseDto = UserResponseDtoBuilder.buildUserResponseDtoFromUsers(user);
            userResponseDtoList.add(userResponseDto);
        }
        return userResponseDtoList;
    }
}
