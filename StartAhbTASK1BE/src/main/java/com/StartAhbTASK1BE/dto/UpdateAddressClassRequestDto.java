package com.task1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAddressClassRequestDto {

    private long addressId;

    private String userHouseNumber;

    private String userStreetName;

    private String userAreaName;

    private String userLandMarkName;

    private String userDistrictName;

    private String userStateName;

    private String userCountryName;

    private int userPinCodeNumber;

}
