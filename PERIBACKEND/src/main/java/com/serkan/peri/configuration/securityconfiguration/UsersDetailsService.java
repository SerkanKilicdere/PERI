package com.serkan.peri.configuration.securityconfiguration;

import com.serkan.peri.entity.user.Users;
import com.serkan.peri.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {

    private final UsersService usersService;




    @Override
    public UserDetails loadUserByUsername(String email ) throws UsernameNotFoundException {
        Users users = usersService.loadByUserName(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .authorities(users.getUserCategories().name())
                .build();
    }




    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        Users users = usersService.loadByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Users not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .authorities(users.getUserCategories().name())
                .build();
    }






    }

