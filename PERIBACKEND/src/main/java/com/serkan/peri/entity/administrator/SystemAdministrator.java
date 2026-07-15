package com.serkan.peri.entity.administrator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.serkan.peri.entity.BaseEntity;
import com.serkan.peri.entity.company.Company;
import com.serkan.peri.entity.user.Users;
import com.serkan.peri.entity.utility.userutilities.UserCategories;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class SystemAdministrator extends Users {



    @Enumerated(jakarta.persistence.EnumType.STRING)
    private UserCategories userCategories=UserCategories.APPLICATION_OWNER;


@OneToMany(mappedBy = "systemAdministrator")
@JsonManagedReference // Döngüyü kırmak için eklendi
private List<Company> affiliatedCompanies = new ArrayList<>();

    @Override
    public void setUserCategories(UserCategories userCategories) {
        super.setUserCategories(userCategories);
        this.userCategories = userCategories;
    }

    @Override
    public UserCategories getUserCategories() {
        return this.userCategories != null ? this.userCategories : super.getUserCategories();
    }

    @PrePersist
    @PreUpdate
    @PostLoad
    private void syncUserCategories() {
        UserCategories effective = this.userCategories != null ? this.userCategories : super.getUserCategories();
        if (effective == null) {
            effective = UserCategories.APPLICATION_OWNER;
        }
        this.userCategories = effective;
        super.setUserCategories(effective);
    }
}


