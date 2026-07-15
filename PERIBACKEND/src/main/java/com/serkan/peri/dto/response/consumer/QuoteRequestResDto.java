package com.serkan.peri.dto.response.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuoteRequestResDto(
        UUID id,
        String fullName,
        String email,
        String phone,
        String companyName,
        int employeeCount,
        String notes,
        boolean read,
        LocalDateTime createdAt
) {}
