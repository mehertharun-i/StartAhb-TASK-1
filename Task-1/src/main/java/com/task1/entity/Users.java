package com.task1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String userFirstName;

    @Column(nullable = false)
    private String userLastName;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false, unique = true)
    private String userPhoneNumber;

    private Date userDateOfBirth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<AddressClass> addressClass;

    @Column(nullable = false, unique = true)
    private String userLoginId;

    @Column(nullable = false)
    private String userPassword;

    public Users(String userFirstName, String userLastName, String userEmail, String userPhoneNumber,
                     Date userDateOfBirth, List<AddressClass> addressClass, String userLoginId, String userPassword) {
        super();
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userDateOfBirth = userDateOfBirth;
        this.addressClass = addressClass;
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
    }


}
