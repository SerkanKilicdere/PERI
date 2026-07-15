package com.serkan.peri.utility.emailsender;

import com.serkan.peri.entity.administrator.SystemAdministrator;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.repositorty.administrator.SystemAdministratorRepository;
import com.serkan.peri.repositorty.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SystemAdministratorEmailService {

    private final EmailTemplateService templateService;
    private final CompanyRepository companyRepository;
    private final SystemAdministratorRepository systemAdministratorRepository;
    private final SystemAdministratorEMailSenderRepository systemAdministratorEMailSenderRepository;
    private final SystemAdministratorCoreEmailService systemAdministratorCoreEmailService;


    @Value("${app.base-url-backend:http://localhost:9090}")
    private String baseUrlBackEnd;

    @Transactional
    public boolean verifyCompanyEMailTokenAndActivateCompany(String companyVerificationEMailToken) {
        Optional<SystemAdministratorEMailSender> tokenRecord = systemAdministratorEMailSenderRepository.findByCompanyVerificationEMailToken(companyVerificationEMailToken);
        if (tokenRecord.isEmpty()) return false;

        SystemAdministratorEMailSender recordedToken = tokenRecord.get();
        if (recordedToken.getDuration().before(new Date())) return false;
        if (recordedToken.getCompanyId() == null) return false;
        if (recordedToken.getSenderSystemAdministratorId() == null) return false;

        return companyRepository.findById(recordedToken.getCompanyId())
                .map(company -> {
                    if (company.getSystemAdministrator() == null || !company.getSystemAdministrator().getId().equals(recordedToken.getSenderSystemAdministratorId())) {
                        return false;
                    }
                    companyRepository.save(company);
                    return true;
                }).orElse(false);
    }
    @Transactional
    public void sendCompanyVerificationEmail(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Şirket bilgisi boş olamaz.");
        }
        if (company.getSystemAdministrator() == null) {
            throw new RuntimeException("Doğrulama işlemi için şirkete atanmış bir sistem yöneticisi bulunmalıdır.");
        }

        String token = UUID.randomUUID().toString();

        SystemAdministratorEMailSender companyEMailRecord = new SystemAdministratorEMailSender();
        companyEMailRecord.setCompanyId(company.getId());
        companyEMailRecord.setSenderSystemAdministratorId(company.getSystemAdministrator().getId());
        companyEMailRecord.setCompanyVerificationEMailToken(token);
        companyEMailRecord.setDuration(Date.from(Instant.now().plus(Duration.ofHours(48))));
        systemAdministratorEMailSenderRepository.save(companyEMailRecord);

        String verifyLink = baseUrlBackEnd + "/dev/v1/systemadministrator/verifycompanyemail?token=" + token;
        String htmlBody = templateService.generateActionTemplate(
                company.getSmtpUsername(),
                "Hesabınızı Doğrulayın",
                "PERİ İK sistemine kaydınız alınmıştır. Hesabınızı aktifleştirmek için lütfen aşağıdaki butona tıklayınız:",
                verifyLink,
                "Doğrula"
        );

        systemAdministratorCoreEmailService.sendHtmlEmail(company.getFromEmail(), "Email Doğrulama", htmlBody, company.getId());
    }

    public void sendContactEmail(String senderName, String senderEmail, String message) {
        SystemAdministrator sysAdmin = systemAdministratorRepository.findAllByOrderByIdAsc()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Sistem yöneticisi bulunamadı."));

        Company smtpCompany = sysAdmin.getAffiliatedCompanies().stream()
                .filter(c -> c.getSmtpHost() != null && !c.getSmtpHost().isBlank()
                        && c.getSmtpUsername() != null && !c.getSmtpUsername().isBlank()
                        && c.getSmtpPassword() != null && !c.getSmtpPassword().isBlank()
                        && c.getFromEmail() != null && !c.getFromEmail().isBlank())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sistem yöneticisine ait geçerli SMTP ayarı bulunamadı."));

        String htmlBody = templateService.generateContactTemplate(senderName, senderEmail, message);
        systemAdministratorCoreEmailService.sendHtmlEmail(sysAdmin.getEmail(), "İletişim Formu: " + senderName, htmlBody, smtpCompany.getId());
    }
}