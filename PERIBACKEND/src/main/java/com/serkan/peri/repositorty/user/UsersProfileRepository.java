package com.serkan.peri.repositorty.user;

import com.serkan.peri.entity.user.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersProfileRepository extends JpaRepository<UsersProfile, UUID> {
    Optional<UsersProfile> findByUsers_Id(UUID userId);
}

