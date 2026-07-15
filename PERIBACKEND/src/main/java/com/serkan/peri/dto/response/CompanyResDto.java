package com.serkan.peri.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompanyResDto(
        @NotBlank
        @Email
        String smtpUsername,

        @NotBlank
        String taxNumber, // Vergi Numarası

        @NotBlank

        String registrationNumber // Ticaret Sicil No
) {
}
