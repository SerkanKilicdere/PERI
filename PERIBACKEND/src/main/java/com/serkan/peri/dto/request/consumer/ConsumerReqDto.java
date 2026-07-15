package com.serkan.peri.dto.request.consumer;

import com.serkan.peri.entity.utility.consumerutilities.MemberShipStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record ConsumerReqDto(

        @NotBlank
        String companyName,

        @NotBlank
        @Email
        String smtpUsername,

        @NotBlank
        String smtpHost,

        @NotNull
        @Min(1)
        @Max(65535)
        Integer smtpPort,

        @NotBlank
        String smtpPassword,

        @NotBlank
        @Email
        String fromEmail,

        @NotBlank
        String taxNumber, // Vergi Numarası

        @NotBlank
        String registrationNumber, // Ticaret Sicil No
          @Enumerated(EnumType.STRING)
        MemberShipStatus memberShipStatus


) {
}
