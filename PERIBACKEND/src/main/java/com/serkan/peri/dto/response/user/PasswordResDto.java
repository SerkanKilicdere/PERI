package com.serkan.peri.dto.response.user;

import com.serkan.peri.entity.utility.userutilities.UserCategories;

public record PasswordResDto(
        String firstName,

        String lastName,

        String phoneNumber,

        String emailAddress,

        UserCategories userCategories





) {
}
