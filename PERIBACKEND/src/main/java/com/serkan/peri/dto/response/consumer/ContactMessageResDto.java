package com.serkan.peri.dto.response.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContactMessageResDto(
        UUID id,
        String fullName,
        String senderEmail,
        String message,
        boolean read,
        LocalDateTime createdAt
) {}
