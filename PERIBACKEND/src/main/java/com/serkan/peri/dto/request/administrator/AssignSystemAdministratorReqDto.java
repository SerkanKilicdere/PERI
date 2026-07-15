package com.serkan.peri.dto.request.administrator;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignSystemAdministratorReqDto(
        @NotNull
        UUID companyId,
        @NotNull
        UUID systemAdministratorId
) {
}
