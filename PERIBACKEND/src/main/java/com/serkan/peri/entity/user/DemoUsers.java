package com.serkan.peri.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class DemoUsers extends Users {

    private final LocalDateTime demoPeriod= LocalDateTime.now().plusDays(3) ;
    private boolean isExpired;





}


