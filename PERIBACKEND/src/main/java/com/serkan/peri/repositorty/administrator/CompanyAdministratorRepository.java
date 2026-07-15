package com.serkan.peri.repositorty.administrator;

import com.serkan.peri.entity.administrator.CompanyAdministrator;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CompanyAdministratorRepository extends CrudRepository<CompanyAdministrator, UUID> {

}
