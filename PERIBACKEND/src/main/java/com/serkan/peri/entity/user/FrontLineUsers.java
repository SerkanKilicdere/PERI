package com.serkan.peri.entity.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class FrontLineUsers extends Users {
    private String shiftPattern; // Vardiya (08:00-16:00 vb.)
    private String workZoneId; // Saha/Bant numarası
    private boolean heavyMachineryLicense; // Forklift vb. ehliyeti
    private LocalDate lastSafetyTrainingDate; // Son İSG eğitimi
    private String emergencyContactPhone;

}