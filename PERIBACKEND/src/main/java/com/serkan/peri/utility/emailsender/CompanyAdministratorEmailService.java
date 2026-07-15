package com.serkan.peri.utility.emailsender;

import com.serkan.peri.entity.company.Company;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.repositorty.company.CompanyRepository;
import com.serkan.peri.repositorty.user.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyAdministratorEmailService {

    private final EmailTemplateService templateService;
    private final UsersRepository usersRepository;
    private final CompanyAdministratorEmailSenderRepository emailRepository;
    private final CompanyRepository companyRepository;
    private final CompanyAdministratorCoreEmailService companyAdministratorCoreEmailService;

    @Value("${app.base-url-backend:http://localhost:9090}")
    private String baseUrlBackEnd;

    @Value("${app.base-url-frontend:http://localhost:5173}")
    private String baseUrlFrontEnd;

    /**
     * PERSONEL DOĞRULAMA (Şirketin kendi tanımladığı özel SMTP maili üzerinden gider)
     */
    @Transactional
    public void sendVerificationEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));

        Company company = companyRepository.findById(users.getCompany().getId())
                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı."));
        if (company.getCompanyAdministrator() == null) {
            throw new RuntimeException("Personel işlemleri için şirkete atanmış bir şirket yöneticisi bulunmalıdır.");
        }

        if (company.getSmtpUsername() == null || company.getSmtpUsername().isBlank()) {
            throw new IllegalStateException("Personel işlemlerine başlayabilmek için lütfen önce SMTP ayarlarınızı tamamlayınız!");
        }

        String token = UUID.randomUUID().toString();

        CompanyAdministratorEmailSender emailRecord = new CompanyAdministratorEmailSender();
        emailRecord.setEmail(email);
        emailRecord.setTargetUserId(users.getId());
        emailRecord.setCompanyId(company.getId());
        emailRecord.setSenderCompanyAdministratorId(company.getCompanyAdministrator().getId());
        emailRecord.setVerificationEmailToken(token);
        emailRecord.setDuration(Date.from(Instant.now().plus(Duration.ofHours(48))));
        emailRepository.save(emailRecord);

        String verifyLink = baseUrlBackEnd + "/dev/v1/humanresource/verifyemail?token=" + token;
        String htmlBody = templateService.generateActionTemplate(
                users.getUserName(),
                "Hesabınızı Doğrulayın",
                "Hesabınızı aktifleştirmek için lütfen aşağıdaki butona tıklayınız:",
                verifyLink,
                "Doğrula"
        );

        companyAdministratorCoreEmailService.sendHtmlEmail(email, "Email Doğrulama", htmlBody, company.getId());
    }

    @Transactional
    public boolean verifyTokenAndActivateUser(String token) {
        Optional<CompanyAdministratorEmailSender> tokenRecord = emailRepository.findByVerificationEmailToken(token);
        if (tokenRecord.isEmpty()) return false;

        CompanyAdministratorEmailSender recordedToken = tokenRecord.get();
        if (recordedToken.getDuration().before(new Date())) return false;
        if (recordedToken.getTargetUserId() == null) return false;
        if (recordedToken.getCompanyId() == null) return false;
        if (recordedToken.getSenderCompanyAdministratorId() == null) return false;

        Optional<Company> companyOptional = companyRepository.findById(recordedToken.getCompanyId());
        if (companyOptional.isEmpty()) return false;
        Company company = companyOptional.get();
        if (company.getCompanyAdministrator() == null || !company.getCompanyAdministrator().getId().equals(recordedToken.getSenderCompanyAdministratorId())) {
            return false;
        }

        return usersRepository.findById(recordedToken.getTargetUserId())
                .map(user -> {
                    if (user.getCompany() == null || !user.getCompany().getId().equals(recordedToken.getCompanyId())) {
                        return false;
                    }
                    user.setActive(true);
                    usersRepository.save(user);
                    return true;
                }).orElse(false);
    }

    /**
     * PERSONEL ŞİFRE OLUŞTURMA (Şirketin kendi tanımladığı özel SMTP maili üzerinden gider)
     */
    @Transactional
    public void sendCreatePasswordEmail(Users users) {
        String createPasswordLink = baseUrlFrontEnd + "/createpassword?email=" + users.getEmail();

        String htmlBody = templateService.generateActionTemplate(
                users.getUserName(),
                "Şifrenizi Oluşturun",
                "Hesabınız onaylanmıştır. Sisteme giriş yapabilmek için lütfen şifrenizi belirleyin:",
                createPasswordLink,
                "Şifre Oluştur"
        );

        Company company = companyRepository.findById(users.getCompany().getId())
                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı."));
        if (company.getCompanyAdministrator() == null) {
            throw new RuntimeException("Şifre oluşturma işlemi için şirkete atanmış bir şirket yöneticisi bulunmalıdır.");
        }

        companyAdministratorCoreEmailService.sendHtmlEmail(users.getEmail(), "Şifre Oluşturma Bildirimi", htmlBody, company.getId());
    }



}