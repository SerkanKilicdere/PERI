package com.serkan.peri.service.company;

import com.serkan.peri.dto.request.consumer.ConsumerReqDto;
import com.serkan.peri.entity.administrator.CompanyAdministrator;
import com.serkan.peri.entity.administrator.SystemAdministrator;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.entity.utility.consumerutilities.MemberShipStatus;
import com.serkan.peri.entity.utility.userutilities.UserCategories;
import com.serkan.peri.utility.emailsender.SystemAdministratorEmailService;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.repositorty.administrator.CompanyAdministratorRepository;
import com.serkan.peri.repositorty.administrator.SystemAdministratorRepository;
import com.serkan.peri.repositorty.company.CompanyRepository;
import com.serkan.peri.repositorty.user.UsersRepository;
import com.serkan.peri.utility.emailsender.CompanyAdministratorEmailService;
import com.serkan.peri.utility.emailsender.CompanyAdministratorEmailSenderRepository;
import com.serkan.peri.utility.emailsender.SystemAdministratorEMailSenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int GMAIL_SMTP_PORT = 587;

    private final SystemAdministratorEmailService systemAdministratorEmailService;
    private final CompanyRepository companyRepository;
    private final CompanyAdministratorEmailService companyAdministratorEmailService;
    private final SystemAdministratorRepository systemAdministratorRepository;
    private final CompanyAdministratorRepository companyAdministratorRepository;
    private final UsersRepository usersRepository;
    private final CompanyAdministratorEmailSenderRepository companyAdministratorEmailSenderRepository;
    private final SystemAdministratorEMailSenderRepository systemAdministratorEMailSenderRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean isValidtaxNumber(String taxNumber) {
        if (taxNumber == null || taxNumber.length() != 10 || !taxNumber.matches("\\d+")) {
            return false;
        }

        int sum = 0;
        int lastDigit = Character.getNumericValue(taxNumber.charAt(9));

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(taxNumber.charAt(i));

    
            int v1 = (digit + 10 - (i + 1)) % 10;

            int v2 = 0;
            if (v1 != 0) {
                
                v2 = (int) ((v1 * Math.pow(2, 10 - (i + 1))) % 9);
                if (v2 == 0) {
                    v2 = 9;
                }
            }

            sum += v2;
        }

        int computedLastDigit = (10 - (sum % 10)) % 10;

    
        return computedLastDigit == lastDigit;

    }



    public Company saveCompany(ConsumerReqDto consumerReqDto) {
        if (!GMAIL_SMTP_HOST.equalsIgnoreCase(consumerReqDto.smtpHost().trim())
                || consumerReqDto.smtpPort() != GMAIL_SMTP_PORT) {
            throw new RuntimeException("Bu sürümde yalnızca Gmail SMTP (smtp.gmail.com:587) desteklenmektedir.");
        }

        Company company = new Company();
        company.setCompanyName(consumerReqDto.companyName());
        company.setSmtpHost(GMAIL_SMTP_HOST);
        company.setSmtpPort(GMAIL_SMTP_PORT);
        company.setSmtpUsername(consumerReqDto.smtpUsername().trim());
        company.setSmtpPassword(consumerReqDto.smtpPassword().trim());
        company.setTaxNumber(consumerReqDto.taxNumber());
        company.setRegistrationNumber(consumerReqDto.registrationNumber());
        company.setMemberShipStatus(consumerReqDto.memberShipStatus());
        company.setFromEmail(consumerReqDto.fromEmail().trim());
        company.setSystemAdministrator(selectAutoSystemAdministrator());

        if (consumerReqDto.memberShipStatus().equals(MemberShipStatus.STARTER)){
            company.setMaxUserLimit(15);
        } else if (consumerReqDto.memberShipStatus().equals(MemberShipStatus.PROFESSIONAL)) {
            company.setMaxUserLimit(50);
        }

      
        Company savedCompany = companyRepository.save(company);
        CompanyAdministrator savedCompanyAdministrator = createAndSaveInitialCompanyAdministrator(savedCompany, consumerReqDto.smtpPassword().trim());
        savedCompany.setCompanyAdministrator(savedCompanyAdministrator);
        savedCompany = companyRepository.save(savedCompany);


        systemAdministratorEmailService.sendCompanyVerificationEmail(savedCompany);

        return savedCompany;
    }

    @Transactional
    public Company assignSystemAdministratorToCompany(UUID companyId, UUID systemAdministratorId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı."));
        SystemAdministrator systemAdministrator = systemAdministratorRepository.findById(systemAdministratorId)
                .orElseThrow(() -> new RuntimeException("Sistem yöneticisi bulunamadı."));
        company.setSystemAdministrator(systemAdministrator);
        return companyRepository.save(company);
    }

    @Transactional
    public Company assignCompanyAdministratorToCompany(UUID companyId, UUID companyAdministratorId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı."));
        CompanyAdministrator companyAdministrator = companyAdministratorRepository.findById(companyAdministratorId)
                .orElseThrow(() -> new RuntimeException("Şirket yöneticisi bulunamadı."));
        company.setCompanyAdministrator(companyAdministrator);
        return companyRepository.save(company);
    }

    private SystemAdministrator selectAutoSystemAdministrator() {
        List<SystemAdministrator> systemAdministrators = systemAdministratorRepository.findAllByOrderByIdAsc();
        if (systemAdministrators.isEmpty()) {
            throw new RuntimeException("Atama için kullanılabilir sistem yöneticisi bulunamadı.");
        }

        return systemAdministrators.stream()
                .min(Comparator
                        .comparingLong((SystemAdministrator admin) -> companyRepository.countBySystemAdministrator_Id(admin.getId()))
                        .thenComparing(SystemAdministrator::getId))
                .orElseThrow(() -> new RuntimeException("Sistem yöneticisi otomatik ataması yapılamadı."));
    }


    public Optional<Company> findByCompanyEMail(String companyEMail) {
        return companyRepository.findByFromEmail(companyEMail.trim());
    }

    @Transactional
    public void deleteCompanyWithDependencies(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Silinecek şirket bulunamadı."));

        CompanyAdministrator companyAdministrator = company.getCompanyAdministrator();
        UUID companyAdministratorId = companyAdministrator != null ? companyAdministrator.getId() : null;

        companyAdministratorEmailSenderRepository.deleteByCompanyId(companyId);
        systemAdministratorEMailSenderRepository.deleteByCompanyId(companyId);

        List<Users> companyUsers = usersRepository.findAllByCompany_Id(companyId);
        for (Users user : companyUsers) {
            if (user instanceof SystemAdministrator || user instanceof CompanyAdministrator) {
                user.setCompany(null);
                usersRepository.save(user);
            } else {
                usersRepository.delete(user);
            }
        }

        company.setCompanyAdministrator(null);
        company.setSystemAdministrator(null);
        companyRepository.save(company);
        companyRepository.delete(company);

        if (companyAdministratorId != null
                && companyRepository.countByCompanyAdministrator_Id(companyAdministratorId) == 0) {
            companyAdministratorRepository.findById(companyAdministratorId)
                    .ifPresent(companyAdministratorRepository::delete);
        }
    }

    private CompanyAdministrator createAndSaveInitialCompanyAdministrator(Company company, String rawPassword) {
        String adminEmail = company.getFromEmail().trim();
        if (usersRepository.findByEmail(adminEmail).isPresent()) {
            throw new RuntimeException("Bu e-posta adresiyle kayıtlı bir kullanıcı zaten mevcut: " + adminEmail);
        }

        CompanyAdministrator companyAdministrator = new CompanyAdministrator();
        companyAdministrator.setFirstName(company.getCompanyName());
        companyAdministrator.setLastName("Admin");
        companyAdministrator.setPhoneNumber("0000000000");
        companyAdministrator.setEmail(adminEmail);
        companyAdministrator.setUserName(adminEmail);
        companyAdministrator.setPassword(passwordEncoder.encode(rawPassword));
        companyAdministrator.setUserCategories(UserCategories.TECH_IT);
        companyAdministrator.setCompany(company);
        companyAdministrator.setActive(true);
        companyAdministrator.setActivated(true);
        return companyAdministratorRepository.save(companyAdministrator);
    }

}
