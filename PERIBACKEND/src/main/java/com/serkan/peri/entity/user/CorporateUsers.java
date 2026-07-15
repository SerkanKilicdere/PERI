package com.serkan.peri.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity

@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class CorporateUsers extends Users {
    private String budgetCenterCode; // Maliyet merkezi
    private String professionalLicense; // Baro no, SMMM no, İK sertifikası vb.
    private String labAccessCode; // Ar-Ge için laboratuvar yetkisi
    private String legalExpertiseArea; // Uzmanlık alanı

}