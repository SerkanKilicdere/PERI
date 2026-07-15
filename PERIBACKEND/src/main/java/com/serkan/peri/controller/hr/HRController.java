package com.serkan.peri.controller.hr;

import com.serkan.peri.configuration.exceptionconfiguration.ApplicationSpesificException;
import com.serkan.peri.dto.request.hr.RecordUserReqDto;
import com.serkan.peri.dto.request.user.PasswordReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.dto.response.hr.RecordUserResDto;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.repositorty.user.UsersRepository;
import com.serkan.peri.service.user.UsersService;
import com.serkan.peri.utility.emailsender.CompanyAdministratorEmailService;
import com.serkan.peri.utility.emailsender.CompanyAdministratorEmailSender;
import com.serkan.peri.utility.emailsender.CompanyAdministratorEmailSenderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import java.util.UUID;

import static com.serkan.peri.utility.ApiPaths.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(HUMANRESOURCE)
public class HRController {
    private final UsersService usersService;
    private final CompanyAdministratorEmailSenderRepository companyAdministratorEmailSenderRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyAdministratorEmailService companyAdministratorEmailService;






    @PostMapping(RECORDUSER)
    public ResponseEntity<BaseResponse<RecordUserResDto>> recordUser(@RequestBody @Valid UUID companyId, @RequestBody @Valid RecordUserReqDto recordUserReqDto) {
        try {
            Users recordedUsers = usersService.record(companyId,recordUserReqDto);
            RecordUserResDto recordUserResDto = new RecordUserResDto(
                    recordedUsers.getFirstName(),
                    recordedUsers.getLastName(),
                    recordedUsers.getPhoneNumber(),
                    recordedUsers.getEmail(),
                    recordedUsers.getUserCategories()
            );
            companyAdministratorEmailService.sendVerificationEmail(recordUserReqDto.emailAddress());
            return ResponseEntity.ok(BaseResponse.<RecordUserResDto>builder()
                    .message("Users registration successful")
                    .data(recordUserResDto)
                    .code(200)
                    .build());
        }catch(ApplicationSpesificException exception){
            return ResponseEntity.ok(BaseResponse.<RecordUserResDto>builder()
                    .message(exception.getErrorType().getErrorMessage())
                    .code(exception.getErrorType().getApplicationErrorCode())
                    .build());
        }

    }

    @GetMapping(VERIFY)
    public RedirectView verifyEmail(@RequestParam("token") String token) {
  
        boolean isValid = companyAdministratorEmailService.verifyTokenAndActivateUser(token);

        if (isValid) {
           
            return new RedirectView("http://localhost:5173/register?token=" + token);
        } else {
            return new RedirectView("http://localhost:5173/dev/v1/error-page");
        }
    }

    @Transactional
    @PostMapping(REGISTER)
    public ResponseEntity<?> completeRegistration(
            
            @RequestBody @Valid PasswordReqDto passwordReqDto) {

   
        CompanyAdministratorEmailSender emailRecord = companyAdministratorEmailSenderRepository.findByVerificationEmailToken(passwordReqDto.token())
                .orElseThrow(() -> new RuntimeException("Geçersiz token!"));

       
        if (!passwordReqDto.password().equals(passwordReqDto.rePassword())) {
            return ResponseEntity.badRequest().body("Şifreler eşleşmiyor!");
        }

        Users users = usersRepository.findById(emailRecord.getTargetUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        if (users.getCompany() == null
                || emailRecord.getCompanyId() == null
                || !users.getCompany().getId().equals(emailRecord.getCompanyId())
                || emailRecord.getSenderCompanyAdministratorId() == null
                || users.getCompany().getCompanyAdministrator() == null
                || !users.getCompany().getCompanyAdministrator().getId().equals(emailRecord.getSenderCompanyAdministratorId())) {
            throw new RuntimeException("Bu doğrulama kaydı atanmış şirket yöneticisi ile eşleşmiyor.");
        }

       
        users.setPassword(passwordEncoder.encode(passwordReqDto.password()));
        users.setActivated(true);
        usersRepository.save(users);

        
        companyAdministratorEmailSenderRepository.delete(emailRecord);

        return ResponseEntity.ok("Hesabınız başarıyla oluşturuldu.");
    }


}
