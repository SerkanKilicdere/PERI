package com.serkan.peri.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class OperationsUsers extends Users {
    private String plantId; // Fabrika ID
    private String logisticsSystemCode; // SAP/ERP depo kodu
    private String qualityInspectorCert; // Kalite kontrol sertifika no
}