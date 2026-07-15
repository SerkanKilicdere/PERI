package com.serkan.peri.entity.company;

import com.serkan.peri.entity.BaseEntity;
import com.serkan.peri.entity.administrator.CompanyAdministrator;
import com.serkan.peri.entity.administrator.SystemAdministrator;

import com.serkan.peri.entity.user.Users;
import com.serkan.peri.entity.utility.consumerutilities.MemberShipStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Company extends BaseEntity {

    private String companyName;

    // Company entity sınıfının içerisine eklenecek alanlar:
    private String smtpHost;
    private Integer smtpPort;
    @Email
    private String smtpUsername; // serkan.kilicdere@outlook.com
    @Column(nullable = true)
    private String smtpPassword; // Şirketin oluşturduğu uygulama şifresi
    @Email
    @Column(nullable = false)
    private String fromEmail;    // serkan.kilicdere@outlook.com







    @Column(unique = true)
    private String taxNumber; // Vergi Numarası

    private String registrationNumber; // Ticaret Sicil No

    private String website;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberShipStatus memberShipStatus;

    private int currentUserCount;

    @Column(name = "max_user_limit", nullable = false)
    private int maxUserLimit;
// com.serkan.peri.entity.company.Company sınıfının içine eklenecek alanlar:

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_admin_id")
    private SystemAdministrator systemAdministrator; // Atanan/sorumlu sistem yöneticisi (tekil referans, sistemde birden çok admin olabilir)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_admin_id")
    private CompanyAdministrator companyAdministrator; // Atanan/sorumlu şirket yöneticisi (tekil referans, şirkette birden çok admin olabilir)

   
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyBranch> branches = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> users = new ArrayList<>();
    public void addBranch(CompanyBranch branch) {
        branches.add(branch);
        branch.setCompany(this);
    }
    public int getCurrentUserCount() {
        return this.users != null ? this.users.size() : 0;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setSystemAdministrator(SystemAdministrator systemAdministrator) {
        this.systemAdministrator = systemAdministrator;
    }

    public void setMaxUserLimit(int maxUserLimit) {
        this.maxUserLimit = maxUserLimit;
    }

    public void setCompanyAdministrator(CompanyAdministrator companyAdministrator) {
        this.companyAdministrator = companyAdministrator;
    }
}
