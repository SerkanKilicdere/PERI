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
@Table(name = "system_administrator_email_sender_records")
public class SystemAdministratorEMailSender extends BaseEntity {
    private UUID companyId;
    private UUID senderSystemAdministratorId;

    private String companyVerificationEMailToken;

    private Date duration;
}
