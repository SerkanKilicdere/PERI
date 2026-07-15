package com.serkan.peri.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class TechnicalUsers extends Users {
    private String techStack;
    private String securityClearanceLevel; // Veri erişim sınıfı
    private String gitId;
    private String serverAccessRole; // Sunucu erişim rolü

}