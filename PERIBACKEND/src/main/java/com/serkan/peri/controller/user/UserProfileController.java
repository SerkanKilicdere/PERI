package com.serkan.peri.controller.user;

import com.serkan.peri.dto.request.user.UpdateUserProfileReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.dto.response.user.UserProfileResDto;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.entity.user.UsersProfile;
import com.serkan.peri.repositorty.user.UsersProfileRepository;
import com.serkan.peri.service.user.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.serkan.peri.utility.ApiPaths.PROFILE;
import static com.serkan.peri.utility.ApiPaths.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UsersService usersService;
    private final UsersProfileRepository usersProfileRepository;

    @GetMapping(PROFILE)
    public ResponseEntity<BaseResponse<UserProfileResDto>> getProfile() {
        Users currentUser = getCurrentUser();
        UsersProfile usersProfile = getOrCreateProfile(currentUser);
        return ResponseEntity.ok(BaseResponse.<UserProfileResDto>builder()
                .code(200)
                .message("Profil bilgisi getirildi.")
                .data(toDto(currentUser, usersProfile))
                .build());
    }

    @PutMapping(PROFILE)
    public ResponseEntity<BaseResponse<UserProfileResDto>> updateProfile(
            @RequestBody @Valid UpdateUserProfileReqDto reqDto
    ) {
        Users currentUser = getCurrentUser();
        UsersProfile usersProfile = getOrCreateProfile(currentUser);
        currentUser.setFirstName(reqDto.firstName().trim());
        currentUser.setLastName(reqDto.lastName().trim());
        currentUser.setPhoneNumber(reqDto.phoneNumber().trim());
        usersProfile.setImageUrl(normalizeNullable(reqDto.imageUrl()));
        usersProfile.setHomeAddress(normalizeNullable(reqDto.homeAddress()));
        usersProfile.setIban(normalizeNullable(reqDto.iban()));
        usersProfile.setBankName(normalizeNullable(reqDto.bankName()));
        usersProfile.setBankAccountNumber(normalizeNullable(reqDto.bankAccountNumber()));
        usersProfile.setBankAccountType(normalizeNullable(reqDto.bankAccountType()));
        usersProfile.setNumberOfChildren(reqDto.numberOfChildren());
        usersProfile.setNationalId(normalizeNullable(reqDto.nationalId()));
        usersProfile.setBloodType(reqDto.bloodType());
        usersProfile.setEducationLevel(reqDto.educationLevel());
        usersProfile.setGender(reqDto.gender());
        usersProfile.setMaritalStatus(reqDto.maritalStatus());

        usersService.save(currentUser);
        usersProfileRepository.save(usersProfile);

        return ResponseEntity.ok(BaseResponse.<UserProfileResDto>builder()
                .code(200)
                .message("Profil başarıyla güncellendi.")
                .data(toDto(currentUser, usersProfile))
                .build());
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Kullanıcı oturumu bulunamadı.");
        }
        return usersService.loadByUserName(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
    }

    private UsersProfile getOrCreateProfile(Users user) {
        return usersProfileRepository.findByUsers_Id(user.getId())
                .orElseGet(() -> {
                    UsersProfile usersProfile = new UsersProfile();
                    usersProfile.setUsers(user);
                    return usersProfileRepository.save(usersProfile);
                });
    }

    private UserProfileResDto toDto(Users user, UsersProfile usersProfile) {
        return new UserProfileResDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUserCategories(),
                usersProfile.getImageUrl(),
                usersProfile.getHomeAddress(),
                usersProfile.getIban(),
                usersProfile.getBankName(),
                usersProfile.getBankAccountNumber(),
                usersProfile.getBankAccountType(),
                usersProfile.getNumberOfChildren(),
                usersProfile.getNationalId(),
                usersProfile.getBloodType(),
                usersProfile.getEducationLevel(),
                usersProfile.getGender(),
                usersProfile.getMaritalStatus()
        );
    }

    private String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
