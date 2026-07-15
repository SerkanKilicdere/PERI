package com.serkan.peri.controller.administrator;

import com.serkan.peri.dto.request.administrator.AssignCompanyAdministratorReqDto;
import com.serkan.peri.service.company.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.serkan.peri.utility.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(COMPANYADMINISTRATOR)
public class CompanyAdministratorController {

    private final CompanyService companyService;

    @PostMapping(ASSIGNCOMPANYADMIN)
    public ResponseEntity<?> assignCompanyAdministrator(@RequestBody @Valid AssignCompanyAdministratorReqDto reqDto) {
        companyService.assignCompanyAdministratorToCompany(reqDto.companyId(), reqDto.companyAdministratorId());
        return ResponseEntity.ok("Şirket yöneticisi ataması başarıyla güncellendi.");
    }

}
