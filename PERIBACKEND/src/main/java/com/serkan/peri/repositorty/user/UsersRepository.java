package com.serkan.peri.repositorty.user;

import com.serkan.peri.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users,UUID> {


    Optional<Users> findByUserName(String emailAddress);

    Optional<Users> findByEmail(String emailAddress);


    Optional<Users> findOptionalByEmailAndPassword(String emailAddress, String password);

    List<Users> findAllByCompany_Id(UUID companyId);

    @Modifying
    @Transactional
    @Query("update Users u set u.password = :password where u.id = :id")
    int updatePasswordById(@Param("id") UUID id, @Param("password") String password);
}
