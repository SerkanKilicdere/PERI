package com.serkan.peri.dto.request.consumer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordReqDto(

        @NotBlank
        String token,

        @Email(message = "Geçerli bir e-posta adresi giriniz")
        @NotBlank
        String emailAddress,

        @NotBlank
        @Size(min = 8, max = 24)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*\\\\.])(?=\\S+$).{8,}$", message = "Şifre en az 8 karakter olmalı, büyük/küçük harf, rakam ve özel karakter içermelidir")
        String password,
        @NotBlank
        @Size(min = 8, max = 24)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*\\\\.])(?=\\S+$).{8,}$", message = "Şifre en az 8 karakter olmalı, büyük/küçük harf, rakam ve özel karakter içermelidir")
        @NotBlank
        String rePassword
) {



}
