package com.serkan.peri.entity.company;

import com.serkan.peri.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CompanyBranch extends BaseEntity {

    @Column(nullable = false)
    private String branchName;

    private String city;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyDepartment> departments = new ArrayList<>();

    public void addDepartment(CompanyDepartment department) {
        departments.add(department);
        department.setBranch(this);
    }
}