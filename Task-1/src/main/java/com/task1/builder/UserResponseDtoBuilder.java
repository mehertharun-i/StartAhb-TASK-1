package com.task1.builder;

import com.task1.dto.AddressClassResponseDto;
import com.task1.dto.UserResponseDto;
import com.task1.entity.AddressClass;
import com.task1.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class UserResponseDtoBuilder {

    public static UserResponseDto buildUserResponseDtoFromUsers(Users users) {
        return UserResponseDto.builder()
                .userId(users.getUserId())
                .userFirstName(users.getUserFirstName())
                .userLastName(users.getUserLastName())
                .userEmail(users.getUserEmail())
                .userPhoneNumber(users.getUserPhoneNumber())
                .userDateOfBirth(users.getUserDateOfBirth())
                .addressClass(buildListOfAddressResponseDtoFromUsers(users.getAddressClass()))
                .build();
    }

    private static List<AddressClassResponseDto> buildListOfAddressResponseDtoFromUsers(List<AddressClass> addressClassList) {
        List<AddressClassResponseDto> addressClassResponseDtoList = new ArrayList<>();
        for(AddressClass addressClass : addressClassList) {
            AddressClassResponseDto addressClassResponseDto = buildAddressResponseDtoFromAddressClass(addressClass);
            addressClassResponseDtoList.add(addressClassResponseDto);
        }
        return addressClassResponseDtoList;
    }

    private static AddressClassResponseDto buildAddressResponseDtoFromAddressClass(AddressClass addressClass) {
        return AddressClassResponseDto.builder()
                .userHouserNumber(addressClass.getUserHouseNumber())
                .userStreetName(addressClass.getUserStreetName())
                .userAreaName(addressClass.getUserAreaName())
                .userLandMarkName(addressClass.getUserLandMarkName())
                .userDistrictName(addressClass.getUserDistrictName())
                .userStateName(addressClass.getUserStateName())
                .userCountryName(addressClass.getUserCountryName())
                .userPinCodeNumber(addressClass.getUserPinCodeNumber())
                .build();
    }
}
