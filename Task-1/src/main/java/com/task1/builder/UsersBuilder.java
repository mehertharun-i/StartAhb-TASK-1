package com.task1.builder;

import com.task1.dto.*;
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
        return users;
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

    public static Users buildUsersFromUpdatedUserRequestDto(UpdateUserRequestDto updateUserRequestDto, Users users, Long id) {
        users.setUserId(id);
        users.setUserFirstName(updateUserRequestDto.getUserFirstName());
        users.setUserLastName(updateUserRequestDto.getUserLastName());
        users.setUserEmail(updateUserRequestDto.getUserEmail());
        users.setUserPhoneNumber(updateUserRequestDto.getUserPhoneNumber());
        users.setUserDateOfBirth(updateUserRequestDto.getUserDateOfBirth());
        users.setAddressClass(BuildListOfUpdatedAddressFromAddressRequestDto(updateUserRequestDto.getAddressClass(), users.getAddressClass()));
        return users;
    }

    private static List<AddressClass> BuildListOfUpdatedAddressFromAddressRequestDto(List<UpdateAddressClassRequestDto> addressClassRequestDtoList, List<AddressClass> addressClassList) {

        for(int i = 0; i < addressClassRequestDtoList.size(); i++){
            AddressClass addressClass = buildAddressFromAddressClassRequestDtos(addressClassRequestDtoList.get(i), addressClassList.get(i));
            addressClassList.set(addressClassList.indexOf(addressClass), addressClass);   // for Updating on existing list use list.set() instead of list.add() method
        }
        return addressClassList;
    }

    private static AddressClass buildAddressFromAddressClassRequestDtos(UpdateAddressClassRequestDto addressClassRequestDto, AddressClass addressClass) {
        addressClass.setUserHouseNumber(addressClassRequestDto.getUserHouseNumber());
        addressClass.setUserStreetName(addressClassRequestDto.getUserStreetName());
        addressClass.setUserAreaName(addressClassRequestDto.getUserAreaName());
        addressClass.setUserLandMarkName(addressClassRequestDto.getUserLandMarkName());
        addressClass.setUserDistrictName(addressClassRequestDto.getUserDistrictName());
        addressClass.setUserStateName(addressClassRequestDto.getUserStateName());
        addressClass.setUserCountryName(addressClassRequestDto.getUserCountryName());
        addressClass.setUserPinCodeNumber(addressClassRequestDto.getUserPinCodeNumber());
        addressClass.setUserAddressId(addressClass.getUserAddressId());
        return addressClass;
    }


}
