package com.serkan.peri.utility.emailsender;

import com.serkan.peri.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.*;

import java.util.Date;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_administrator_email_sender_records")
public class CompanyAdministratorEmailSender extends BaseEntity {
    private String email;
    private UUID targetUserId;
    private UUID companyId;
    private UUID senderCompanyAdministratorId;

    private String verificationEmailToken;

    private Date duration;
}
