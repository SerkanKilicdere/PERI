package com.serkan.peri.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class CommercialUsers extends Users {
    private String targetRegion; // Hedef bölge
    private Double salesTarget; // Satış kotası
    private String campaignPortfolio; // Sorumlu olduğu markalar
    private boolean companyCarAssigned;

}