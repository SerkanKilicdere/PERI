package com.serkan.peri.repositorty.administrator;

import com.serkan.peri.entity.administrator.SystemAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SystemAdministratorRepository extends JpaRepository<SystemAdministrator, UUID> {
    List<SystemAdministrator> findAllByOrderByIdAsc();
}
