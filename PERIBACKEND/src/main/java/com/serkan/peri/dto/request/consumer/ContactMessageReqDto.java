package com.serkan.peri.dto.request.consumer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactMessageReqDto(
        @NotBlank(message = "Ad soyad boş olamaz")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "E-posta boş olamaz")
        @Email(message = "Geçerli bir e-posta adresi giriniz")
        String senderEmail,

        @NotBlank(message = "Mesaj boş olamaz")
        @Size(max = 2000)
        String message
) {}
