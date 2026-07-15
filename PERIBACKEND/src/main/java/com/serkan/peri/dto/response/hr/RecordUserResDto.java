package com.serkan.peri.dto.response.hr;

import com.serkan.peri.entity.utility.userutilities.UserCategories;


public record RecordUserResDto(

        String firstName,

        String lastName,

        String phoneNumber,

        String emailAddress,

        UserCategories userCategories

) {
}
