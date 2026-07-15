package com.serkan.peri.entity.company;

import com.serkan.peri.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_departments")
@Getter
@Setter
public class CompanyDepartment extends BaseEntity {

    @Column(nullable = false)
    private String departmentName;

    @Column(name = "dept_code")
    private String departmentCode; // Örn: RD-01, HR-05

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private CompanyBranch branch; // Hangi şubeye bağlı?
}