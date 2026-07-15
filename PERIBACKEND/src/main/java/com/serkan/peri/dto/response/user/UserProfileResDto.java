package com.serkan.peri.dto.response.user;

import com.serkan.peri.entity.utility.userutilities.BloodType;
import com.serkan.peri.entity.utility.userutilities.EducationLevel;
import com.serkan.peri.entity.utility.userutilities.Gender;
import com.serkan.peri.entity.utility.userutilities.MaritalStatus;
import com.serkan.peri.entity.utility.userutilities.UserCategories;

import java.util.UUID;

public record UserProfileResDto(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        UserCategories userCategories,
        String imageUrl,
        String homeAddress,
        String iban,
        String bankName,
        String bankAccountNumber,
        String bankAccountType,
        Byte numberOfChildren,
        String nationalId,
        BloodType bloodType,
        EducationLevel educationLevel,
        Gender gender,
        MaritalStatus maritalStatus
) {
}
