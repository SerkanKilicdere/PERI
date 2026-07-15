package com.serkan.peri.dto.request.user;

import com.serkan.peri.entity.utility.userutilities.BloodType;
import com.serkan.peri.entity.utility.userutilities.EducationLevel;
import com.serkan.peri.entity.utility.userutilities.Gender;
import com.serkan.peri.entity.utility.userutilities.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileReqDto(
        @NotBlank
        @Size(min = 2, max = 64)
        String firstName,

        @NotBlank
        @Size(min = 2, max = 64)
        String lastName,

        @NotBlank
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Telefon numarası 10-15 hane arasında olmalıdır.")
        String phoneNumber,

        String imageUrl,
        String homeAddress,
        @Size(max = 26)
        String iban,
        String bankName,
        String bankAccountNumber,
        String bankAccountType,
        Byte numberOfChildren,
        @Pattern(regexp = "^\\d{11}$", message = "TC Kimlik Numarası 11 haneli olmalıdır.")
        String nationalId,
        BloodType bloodType,
        EducationLevel educationLevel,
        Gender gender,
        MaritalStatus maritalStatus
) {
}
