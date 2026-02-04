package com.task1.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private String userPhoneNumber;

    private Date userDateOfBirth;

    private List<AddressClassRequestDto> addressClass;

    private String userLoginId;

    private String userPassword;

}
