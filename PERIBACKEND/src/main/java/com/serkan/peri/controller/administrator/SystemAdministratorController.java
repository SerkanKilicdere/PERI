package com.serkan.peri.controller.administrator;




import com.serkan.peri.dto.request.consumer.PasswordReqDto;
import com.serkan.peri.dto.request.administrator.AssignSystemAdministratorReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.dto.response.consumer.ContactMessageResDto;
import com.serkan.peri.dto.response.consumer.QuoteRequestResDto;
import com.serkan.peri.entity.administrator.CompanyAdministrator;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.entity.consumer.ContactMessage;
import com.serkan.peri.repositorty.administrator.CompanyAdministratorRepository;
import com.serkan.peri.repositorty.company.CompanyRepository;
import com.serkan.peri.repositorty.consumer.ContactMessageRepository;
import com.serkan.peri.repositorty.consumer.QuoteRequestRepository;
import com.serkan.peri.service.company.CompanyService;
import com.serkan.peri.utility.emailsender.SystemAdministratorEMailSender;
import com.serkan.peri.utility.emailsender.SystemAdministratorEMailSenderRepository;
import com.serkan.peri.utility.emailsender.SystemAdministratorEmailService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.serkan.peri.utility.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(SYSTEMADMINISTRATOR)
public class SystemAdministratorController {

    private final SystemAdministratorEmailService systemAdministratorEmailService;
    private final SystemAdministratorEMailSenderRepository systemAdministratorEMailSenderRepository;

    private final CompanyRepository companyRepository;
    private final CompanyAdministratorRepository companyAdministratorRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final ContactMessageRepository contactMessageRepository;
    private final QuoteRequestRepository quoteRequestRepository;

    @Value("${app.base-url-frontend:http://localhost:5173}")
    private String baseUrlFrontEnd;



    @GetMapping(VERIFYCOMPANYEMAIL)
    public RedirectView verifyCompanyEmail(@RequestParam("token") String token) {
        boolean isValid = systemAdministratorEmailService.verifyCompanyEMailTokenAndActivateCompany(token);

        if (isValid) {
            // Doğrudan gitmek istediğiniz URL string'ini yazıyorsunuz
            return new RedirectView(baseUrlFrontEnd + "/registercompany?token=" + token);
        } else {
            return new RedirectView(baseUrlFrontEnd + "/verification-error");
        }
    }

    @PostMapping(ASSIGNSYSTEMADMIN)
    public ResponseEntity<?> assignSystemAdministrator(@RequestBody @Valid AssignSystemAdministratorReqDto reqDto) {
        companyService.assignSystemAdministratorToCompany(reqDto.companyId(), reqDto.systemAdministratorId());
        return ResponseEntity.ok("Sistem yöneticisi ataması başarıyla güncellendi.");
    }

    @DeleteMapping(DELETECOMPANY)
    public ResponseEntity<BaseResponse<Void>> deleteCompany(@PathVariable UUID companyId) {
        try {
            companyService.deleteCompanyWithDependencies(companyId);
            return ResponseEntity.ok(BaseResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Şirket ve bağlı kayıtlar başarıyla silindi.")
                    .build());
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.<Void>builder()
                            .code(5000)
                            .message(exception.getMessage())
                            .build());
        }
    }

    @Transactional
    @PostMapping(REGISTER)
    public ResponseEntity<?> completeRegistration(
          
            @RequestBody @Valid PasswordReqDto passwordReqDto) {


       SystemAdministratorEMailSender   emailRecord = systemAdministratorEMailSenderRepository.findByCompanyVerificationEMailToken(passwordReqDto.token())
                .orElseThrow(() -> new RuntimeException("Geçersiz token!"));

      
        if (!passwordReqDto.password().equals(passwordReqDto.rePassword())) {
            return ResponseEntity.badRequest().body("Şifreler eşleşmiyor!");
        }

       
  Company company = companyRepository.findById(emailRecord.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Şİrket bulunamadı!"));
        if (emailRecord.getSenderSystemAdministratorId() == null
                || company.getSystemAdministrator() == null
                || !company.getSystemAdministrator().getId().equals(emailRecord.getSenderSystemAdministratorId())) {
            throw new RuntimeException("Bu doğrulama kaydı atanmış sistem yöneticisi ile eşleşmiyor.");
        }

       
        company.setSmtpPassword(passwordReqDto.password());
        companyRepository.save(company);
        CompanyAdministrator companyAdministrator = company.getCompanyAdministrator();
        if (companyAdministrator != null) {
            companyAdministrator.setPassword(passwordEncoder.encode(passwordReqDto.password()));
            companyAdministrator.setActivated(true);
            companyAdministratorRepository.save(companyAdministrator);
        }

    
systemAdministratorEMailSenderRepository.delete(emailRecord);

        return ResponseEntity.ok("Hesabınız başarıyla oluşturuldu.");
    }

    @GetMapping(CONTACT_MESSAGES)
    public ResponseEntity<BaseResponse<List<ContactMessageResDto>>> getContactMessages() {
        List<ContactMessageResDto> messages = contactMessageRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(m -> new ContactMessageResDto(m.getId(), m.getFullName(), m.getSenderEmail(), m.getMessage(), m.isRead(), m.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.<List<ContactMessageResDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Başarılı")
                .data(messages)
                .build());
    }

    @PatchMapping(MARK_READ)
    public ResponseEntity<BaseResponse<Void>> markAsRead(@PathVariable UUID id) {
        contactMessageRepository.findById(id).ifPresent(m -> {
            m.setRead(true);
            contactMessageRepository.save(m);
        });
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Okundu olarak işaretlendi.")
                .build());
    }

    @GetMapping(QUOTE_REQUESTS)
    public ResponseEntity<BaseResponse<List<QuoteRequestResDto>>> getQuoteRequests() {
        List<QuoteRequestResDto> list = quoteRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(q -> new QuoteRequestResDto(q.getId(), q.getFullName(), q.getEmail(), q.getPhone(),
                        q.getCompanyName(), q.getEmployeeCount(), q.getNotes(), q.isRead(), q.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.<List<QuoteRequestResDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Başarılı")
                .data(list)
                .build());
    }

    @PatchMapping(QUOTE_MARK_READ)
    public ResponseEntity<BaseResponse<Void>> markQuoteAsRead(@PathVariable UUID id) {
        quoteRequestRepository.findById(id).ifPresent(q -> {
            q.setRead(true);
            quoteRequestRepository.save(q);
        });
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Okundu olarak işaretlendi.")
                .build());
    }

}
