package com.serkan.peri.repositorty.company;

import com.serkan.peri.entity.company.Company;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {


    Optional<Company> findByFromEmail(@Email @NotBlank String companyEMail);
    Optional<Company> findBySmtpUsername(@NotBlank String companyName); // Added this line
    long countBySystemAdministrator_Id(UUID systemAdministratorId);
    long countByCompanyAdministrator_Id(UUID companyAdministratorId);

}
