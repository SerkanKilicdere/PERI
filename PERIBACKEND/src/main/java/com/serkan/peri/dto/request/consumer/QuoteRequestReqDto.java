package com.serkan.peri.dto.request.consumer;

import jakarta.validation.constraints.*;

public record QuoteRequestReqDto(
        @NotBlank @Size(max = 100)
        String fullName,

        @NotBlank @Email
        String email,

        @NotBlank @Size(max = 50)
        String phone,

        @NotBlank @Size(max = 150)
        String companyName,

        @Min(1)
        int employeeCount,

        @Size(max = 1000)
        String notes
) {}
