package com.serkan.peri.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class ExecutiveUsers extends Users {
    private String globalAccessKey; // APP_OWNER için özel yetki anahtarı
    private Double budgetAuthorityLimit; // Onay yetkisi sınırı
    private String representationArea; // Sorumlu olduğu bölge veya kurul
    private boolean hasVetoPower; // Veto yetkisi var mı?

}