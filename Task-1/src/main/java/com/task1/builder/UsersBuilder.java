package com.task1.builder;

import com.task1.dto.AddressClassRequestDto;
import com.task1.dto.UpdateUserRequestDto;
import com.task1.dto.UserRequestDto;
import com.task1.dto.UserResponseDto;
import com.task1.entity.AddressClass;
import com.task1.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersBuilder {

    public static Users buildUsersFromUserRequestDto(UserRequestDto userRequestDto){
        Users users = Users.builder()
                .userFirstName(userRequestDto.getUserFirstName())
                .userLastName(userRequestDto.getUserLastName())
                .userEmail(userRequestDto.getUserEmail())
                .userPhoneNumber(userRequestDto.getUserPhoneNumber())
                .userDateOfBirth(userRequestDto.getUserDateOfBirth())
                .userLoginId(userRequestDto.getUserLoginId())
                .userPassword(userRequestDto.getUserPassword())
                .addressClass(BuildListOfAddressFromAddressRequestDto(userRequestDto.getAddressClass()))
                .build();
        return null;
    }

    private static List<AddressClass> BuildListOfAddressFromAddressRequestDto(List<AddressClassRequestDto> addressClassRequestDtolist){
        List<AddressClass> addressClassList = new ArrayList<>();
        for(AddressClassRequestDto addressClassRequestDto : addressClassRequestDtolist){
            AddressClass addressClass = buildAddressFromAddressClassRequestDto(addressClassRequestDto);
            addressClassList.add(addressClass);
        }
        return addressClassList;
    }

    public static AddressClass buildAddressFromAddressClassRequestDto(AddressClassRequestDto addressClassRequestDto) {
        return AddressClass.builder()
                .userHouseNumber(addressClassRequestDto.getUserHouseNumber())
                .userStreetName(addressClassRequestDto.getUserStreetName())
                .userAreaName(addressClassRequestDto.getUserAreaName())
                .userLandMarkName(addressClassRequestDto.getUserLandMarkName())
                .userDistrictName(addressClassRequestDto.getUserDistrictName())
                .userStateName(addressClassRequestDto.getUserStateName())
                .userCountryName(addressClassRequestDto.getUserCountryName())
                .userPinCodeNumber(addressClassRequestDto.getUserPinCodeNumber())
                .build();
    }

    public static Users buildUsersFromUserRequestDto(UpdateUserRequestDto updateUserRequestDto, Users users) {
        users.setUserFirstName(updateUserRequestDto.getUserFirstName());
        users.setUserLastName(updateUserRequestDto.getUserLastName());
        users.setUserEmail(updateUserRequestDto.getUserEmail());
        users.setUserPhoneNumber(updateUserRequestDto.getUserPhoneNumber());
        users.setUserDateOfBirth(updateUserRequestDto.getUserDateOfBirth());
        users.setAddressClass(BuildListOfAddressFromAddressRequestDto(updateUserRequestDto.getAddressClass()));
        return users;
    }
}
