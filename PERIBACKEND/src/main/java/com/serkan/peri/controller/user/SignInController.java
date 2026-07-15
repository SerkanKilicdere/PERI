package com.serkan.peri.controller.user;

import com.serkan.peri.configuration.securityconfiguration.CreateToken;
import com.serkan.peri.configuration.securityconfiguration.ValidateToken;
import com.serkan.peri.dto.request.user.SignInUserReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.repositorty.user.UsersRepository;
import com.serkan.peri.service.user.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.serkan.peri.utility.ApiPaths.*;


@AllArgsConstructor
@RestController
@RequestMapping(SIGNIN)
public class SignInController {

    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final CreateToken createToken;
    private final ValidateToken validateToken;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;



    @PostMapping(LOGIN)
    public ResponseEntity<BaseResponse<String>> signIn(@RequestBody @Valid SignInUserReqDto dto) {
        
        authenticateUsingCurrentOrLegacyPassword(dto.emailAddress(), dto.password());

        Users users = usersService.loadByUserName(dto.emailAddress())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

    
        String loginToken = createToken.createToken(users.getId(), users.getEmail(), List.of(users.getUserCategories().name()));

        return ResponseEntity.ok(BaseResponse.<String>builder()
                .message("Başarılı")
                .code(200)
                .data(loginToken)
                .build());
    }

    private void authenticateUsingCurrentOrLegacyPassword(String email, String rawPassword) {
        Optional<Users> maybeUser = usersService.loadByUserName(email);
        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı");
        }

        Users user = maybeUser.get();
        String storedPassword = user.getPassword();
        if (storedPassword == null || storedPassword.isBlank()) {
            throw new BadCredentialsException("Bad credentials");
        }

        boolean alreadyHashed = storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$");

        if (alreadyHashed) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword)
            );
            return;
        }

        String normalizedStored = storedPassword.trim();
        String normalizedRaw = rawPassword.trim();
        if (!normalizedStored.equals(normalizedRaw)) {
            throw new BadCredentialsException("Bad credentials");
        }
        usersRepository.updatePasswordById(user.getId(), passwordEncoder.encode(normalizedRaw));
    }
   }
