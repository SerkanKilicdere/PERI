package com.serkan.peri.entity.consumer;

import com.serkan.peri.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quote_requests")
@Getter
@Setter
@NoArgsConstructor
public class QuoteRequest extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false)
    private int employeeCount;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean read = false;
}
