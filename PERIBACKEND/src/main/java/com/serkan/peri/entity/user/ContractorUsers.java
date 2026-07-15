package com.serkan.peri.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class ContractorUsers extends Users {
    private String vendorCompanyName; // Bağlı olduğu taşeron firma
    private LocalDate contractEndDate;
    private String projectCode;
    private boolean remoteAccessPermitted;

}