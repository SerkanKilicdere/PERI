package com.serkan.peri.controller.user;

import com.serkan.peri.dto.request.user.PasswordReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.dto.response.user.PasswordResDto;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.service.user.UsersService;
import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.serkan.peri.utility.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(PASSWORD)
public class PasswordController {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;


@PostMapping(CREATEPASSWORD)
    public ResponseEntity<BaseResponse<PasswordResDto>> createPassword(@RequestBody @Valid PasswordReqDto passwordReqDto){
    if(!passwordReqDto.password().equals(passwordReqDto.rePassword())){
        return ResponseEntity.ok(BaseResponse.<PasswordResDto>builder()
                .code(404)
                .message("Şifreler uyuşmuyor!")
                .build());
    }

    Optional<Users> user= usersService.findByEmail(passwordReqDto);
    if (user.isEmpty()){
        return ResponseEntity.ok(BaseResponse.<PasswordResDto>builder()
                .code(404)
                .message("Kullanıcı bulunamadı!")
                .build());
    }
    user.get().setPassword(passwordEncoder.encode(passwordReqDto.password()));
    usersService.save(user.get());

    return ResponseEntity.ok(BaseResponse.<PasswordResDto>builder()
            .code(200)
            .message("Şifreniz başarıyla oluşturuldu. Giriş yapabilirsiniz.")
            .build());

}


}
