package com.serkan.peri.utility.emailsender;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SystemAdministratorEMailSenderRepository extends JpaRepository<SystemAdministratorEMailSender, UUID> {



    Optional<SystemAdministratorEMailSender> findByCompanyVerificationEMailToken(String companyVerificationEMailToken);
    void deleteByCompanyId(UUID companyId);
}
