package com.task1.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AddressClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userAddressId;

    @Column(nullable = false)
    private String userHouseNumber;

    private String userStreetName;

    @Column(nullable = false)
    private String userAreaName;

    private String userLandMarkName;

    @Column(nullable = false)
    private String userDistrictName;

    @Column(nullable = false)
    private String userStateName;

    @Column(nullable = false)
    private String userCountryName;

    private int userPinCodeNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public AddressClass(String userHouseNumber, String userStreetName, String userAreaName, String userLandMarkName,
                        String userDistrictName, String userStateName, String userCountryName, int userPinCodeNumber, Users user) {
        super();
        this.userHouseNumber = userHouseNumber;
        this.userStreetName = userStreetName;
        this.userAreaName = userAreaName;
        this.userLandMarkName = userLandMarkName;
        this.userDistrictName = userDistrictName;
        this.userStateName = userStateName;
        this.userCountryName = userCountryName;
        this.userPinCodeNumber = userPinCodeNumber;
        this.user = user;
    }

}
