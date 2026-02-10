package com.task1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressClassResponseDto {

    private long addressClassId;

    private String userHouserNumber;

    private String userStreetName;

    private String userAreaName;

    private String userLandMarkName;

    private String userDistrictName;

    private String userStateName;

    private String userCountryName;

    private int userPinCodeNumber;
}
