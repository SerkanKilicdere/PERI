package com.serkan.peri.entity.user;

import com.serkan.peri.entity.utility.userutilities.BloodType;
import com.serkan.peri.entity.utility.userutilities.EducationLevel;
import com.serkan.peri.entity.utility.userutilities.Gender;
import com.serkan.peri.entity.utility.userutilities.MaritalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
public class UsersProfile {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users users;

    private String imageUrl;
    private String homeAddress;
    @Column(length = 26)
    private String iban;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountType;
    private Byte numberOfChildren;
    @Column(length = 11,unique=true)
    private String nationalId;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;


    public String getUserName() {
        return users.getEmail();
    }
}
