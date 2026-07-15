package com.serkan.peri.utility.emailsender;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyAdministratorEmailSenderRepository extends JpaRepository<CompanyAdministratorEmailSender, UUID> {
    Optional<CompanyAdministratorEmailSender> findByVerificationEmailToken(String verificationEmailToken);
    void deleteByCompanyId(UUID companyId);

}
