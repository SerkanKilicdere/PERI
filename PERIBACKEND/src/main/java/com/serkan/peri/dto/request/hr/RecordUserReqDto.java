package com.serkan.peri.dto.request.hr;

import com.serkan.peri.entity.utility.userutilities.UserCategories;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordUserReqDto(

        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @Email(message = "Geçerli bir e-posta adresi giriniz")
        @NotBlank
        String emailAddress,
        @NotNull
        UserCategories userCategories

) {
}
