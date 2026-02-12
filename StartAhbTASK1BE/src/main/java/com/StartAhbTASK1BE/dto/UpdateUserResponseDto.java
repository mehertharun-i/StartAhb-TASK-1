package com.task1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserResponseDto {

    private long userId;

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private String userPhoneNumber;

    private LocalDate userDateOfBirth;

    private List<AddressClassRequestDto> addressClass;
}
