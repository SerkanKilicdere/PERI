package com.serkan.peri.entity.utility.consumerutilities;

import lombok.Getter;

@Getter
public enum MemberShipStatus {

    STARTER(15),
    PROFESSIONAL(50),
    CORPORATE(Integer.MAX_VALUE);

    private final int maxUsers;

    MemberShipStatus(int maxUsers){
        this.maxUsers=maxUsers;
    }


}
