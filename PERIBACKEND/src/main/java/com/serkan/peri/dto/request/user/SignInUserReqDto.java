package com.serkan.peri.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInUserReqDto(

        @Email (message = "Geçerli bir e-posta adresi giriniz")
        @NotBlank(message = "E-posta boş olamaz")

       String emailAddress,



@NotBlank(message = "Şifre boş olamaz")
        String password



) {
}
