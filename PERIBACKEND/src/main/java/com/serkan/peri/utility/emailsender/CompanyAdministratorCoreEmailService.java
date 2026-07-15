package com.serkan.peri.utility.emailsender;

import com.serkan.peri.entity.company.Company;
import com.serkan.peri.repositorty.company.CompanyRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyAdministratorCoreEmailService {

    private final CompanyRepository companyRepository;



    public void sendHtmlEmail(String to, String subject, String htmlContent, UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("E-posta gönderimi için belirtilen şirket bulunamadı! ID: " + companyId));

        if (company.getSmtpHost() == null || company.getSmtpHost().isBlank()
                || company.getSmtpPort() == null
                || company.getSmtpUsername() == null || company.getSmtpUsername().isBlank()
                || company.getSmtpPassword() == null || company.getSmtpPassword().isBlank()
                || company.getFromEmail() == null || company.getFromEmail().isBlank()) {
            throw new RuntimeException("Şirket SMTP ayarları eksik. Lütfen SMTP bilgilerini tamamlayınız.");
        }

        JavaMailSender dynamicMailSender = createDynamicMailSender(company);
        sendHtmlEmail(to, subject, htmlContent, dynamicMailSender, company.getFromEmail());
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent, JavaMailSender javaMailSender, String fromEmail) {
        try {
            MimeMessage message =javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

          javaMailSender.send(message);
            log.info(">[Company] E-posta başarıyla iletildi: {}", to);
        } catch (MailAuthenticationException e) {
            String authDetail = extractCauseDetail(e);
            log.error(">[Company] SMTP kimlik doğrulama hatası ({}): {}", to, authDetail);
            throw new RuntimeException("SMTP kimlik doğrulaması başarısız oldu. Lütfen e-posta, host, port ve uygulama şifresini kontrol ediniz. Teknik detay: " + authDetail);
        } catch (MessagingException e) {
            log.error(">[Company] E-posta gönderiminde teknik hata ({}): {}", to, e.getMessage());
            throw new RuntimeException("Şirket e-postası gönderimi başarısız oldu.");
        }
    }




    private JavaMailSender createDynamicMailSender(Company company) {
        JavaMailSenderImpl dynamicSender = new JavaMailSenderImpl();
        dynamicSender.setHost(company.getSmtpHost().trim());
        dynamicSender.setPort(company.getSmtpPort());
        dynamicSender.setUsername(company.getSmtpUsername().trim());
        dynamicSender.setPassword(company.getSmtpPassword().trim());

        Properties props = dynamicSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth.mechanisms", "LOGIN PLAIN");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        return dynamicSender;
    }

    private String extractCauseDetail(Throwable throwable) {
        Throwable cursor = throwable;
        String lastMessage = throwable.getMessage();
        while (cursor != null) {
            if (cursor.getMessage() != null && !cursor.getMessage().isBlank()) {
                lastMessage = cursor.getMessage();
            }
            cursor = cursor.getCause();
        }
        return lastMessage != null ? lastMessage : "Bilinmeyen SMTP hatası";
    }



}