package com.serkan.peri.entity.user;



import com.serkan.peri.entity.BaseEntity;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.entity.company.CompanyDepartment;
import com.serkan.peri.entity.utility.userutilities.UserCategories;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Entity
// InheritanceType.JOINED: Her alt sınıf için DB'de ayrı tablo oluşturur.
// Bu sayede "TechUser" detayları "users" tablosundan bağımsız ama onunla ilişkilidir.
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Users extends BaseEntity {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phoneNumber;
    @Email (message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank
    private String email;
    @Column(nullable = true)
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserCategories userCategories;
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // Users ana sınıfına eklenecek alan:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private CompanyDepartment department;

    





    boolean Active = false; // 26/05 09:49 serkan güncellendi



    boolean Activated = false; // Email doğrulaması



    public String getUserName() {
        return email;
    }
}
