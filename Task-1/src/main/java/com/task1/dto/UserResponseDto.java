package com.task1.dto;

import com.task1.entity.AddressClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private String userPhoneNumber;

    private Date userDateOfBirth;

    private List<AddressClassResponseDto> addressClass;

}
