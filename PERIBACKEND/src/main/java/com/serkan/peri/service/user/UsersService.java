package com.serkan.peri.service.user;




import com.serkan.peri.configuration.exceptionconfiguration.ApplicationSpesificException;
import com.serkan.peri.configuration.exceptionconfiguration.ErrorType;
import com.serkan.peri.dto.request.hr.RecordUserReqDto;
import com.serkan.peri.dto.request.user.PasswordReqDto;
import com.serkan.peri.dto.request.user.SignInUserReqDto;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.entity.user.CommercialUsers;
import com.serkan.peri.entity.user.ContractorUsers;
import com.serkan.peri.entity.user.CorporateUsers;
import com.serkan.peri.entity.user.DemoUsers;
import com.serkan.peri.entity.user.ExecutiveUsers;
import com.serkan.peri.entity.user.FrontLineUsers;
import com.serkan.peri.entity.user.OperationsUsers;
import com.serkan.peri.entity.user.TechnicalUsers;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.repositorty.company.CompanyRepository;
import com.serkan.peri.repositorty.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;

    public Optional<Users> loadByUserId(UUID userId) {

     return usersRepository.findById(userId);
    }

    public Optional<Users> loadByUserName(String email) {
        return usersRepository.findByEmail(email);
    }



    public void save(Users users) {
        usersRepository.save(users);
    }

    public Users record(UUID companyId, RecordUserReqDto  recordUserReqDto) {
      Company company = companyRepository.findById(companyId)
              .orElseThrow(() -> new IllegalArgumentException("Şirket bulunamadı."));
      if (company.getCompanyAdministrator() == null) {
              throw new IllegalArgumentException("Personel kaydı için önce şirkete bir şirket yöneticisi atanmalıdır.");
          }
          if(company.getCurrentUserCount()>=company.getMaxUserLimit()){
              throw new IllegalArgumentException(String.format(
                      "Kullanıcı eklenemedi! Seçtiğiniz %s paketi için maksimum %d kullanıcı kaydedebilirisiniz. Lütfen paketinizi yükseltin."
                      ,company.getMemberShipStatus().name(), company.getMaxUserLimit()));
          }

        Users users = createUserEntity(recordUserReqDto);
        usersRepository.save(users);

        company.setCurrentUserCount(company.getCurrentUserCount()+1);
        companyRepository.save(company);

        return users;


    }

    public Optional<Users> findByEmail(RecordUserReqDto recordUserReqDto) {
        return usersRepository.findByEmail(recordUserReqDto.emailAddress());
    }


    public Optional<Users> findByEmail(PasswordReqDto passwordReqDto) {
        return usersRepository.findByEmail(passwordReqDto.emailAddress());
    }

    public Optional<Users> findByEmailAddressAndPassword(SignInUserReqDto signInUserReqDto) {
        return usersRepository.findOptionalByEmailAndPassword(signInUserReqDto.emailAddress(), signInUserReqDto.password());
    }

    private Users createUserEntity(RecordUserReqDto recordUserReqDto) {
        if (recordUserReqDto.userCategories() == null) {
            throw new ApplicationSpesificException(ErrorType.PARAMETRE_HATASI);
        }

        Users users = switch (recordUserReqDto.userCategories()) {
            case EXECUTIVE, BOARD_MEMBER, REGIONAL_MANAGER -> new ExecutiveUsers();
            case TECH_IT, DATA_ANALYTICS, CYBER_SECURITY -> new TechnicalUsers();
            case SUPPLY_CHAIN, MANUFACTURING, QUALITY_CONTROL -> new OperationsUsers();
            case GENERAL_LABOR, WAREHOUSE_STAFF, DISTRIBUTION_ENTRY, FACILITY_MAINTENANCE, SEASONAL_WORKER, FIELD_OPERATIVE -> new FrontLineUsers();
            case SALES_RETAIL, MARKETING_BRAND, PUBLIC_RELATIONS -> new CommercialUsers();
            case HUMAN_RESOURCES, FINANCE_TREASURY, LEGAL_COMPLIANCE, SUSTAINABILITY, DEPARTMENT_HEAD, STRATEGIC_PLANNING, RESEARCH_DEV -> new CorporateUsers();
            case CONTRACTOR -> new ContractorUsers();
            case DEMO_USER -> new DemoUsers();
            default -> throw new ApplicationSpesificException(ErrorType.INVALID_USER_CATEGORY);
        };

        users.setFirstName(recordUserReqDto.firstName());
        users.setLastName(recordUserReqDto.lastName());
        users.setPhoneNumber(recordUserReqDto.phoneNumber());
        users.setEmail(recordUserReqDto.emailAddress());
        users.setUserCategories(recordUserReqDto.userCategories());
        return users;
    }


}
