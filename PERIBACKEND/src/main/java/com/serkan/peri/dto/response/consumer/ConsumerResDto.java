package com.serkan.peri.dto.response.consumer;

import jakarta.validation.constraints.NotBlank;

public record ConsumerResDto(

        @NotBlank
        String smtpUsername,

        @NotBlank
        String taxNumber, // Vergi Numarası

        @NotBlank

        String registrationNumber // Ticaret Sicil No





) {
}
