package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum ResourceFormatExceptionEnum {
    INVALID_MAC_ADDRESS("The MAC address format is invalid."),
    INVALID_IP_ADDRESS("The IP address format is invalid.");

    private final String message;
    ResourceFormatExceptionEnum(String message) {
        this.message = message;
    }


}
