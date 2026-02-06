package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum ResourceFormatExceptionEnum {

    INVALID_FORMAT("Invalid format: "),

    // CAMERA
    INVALID_IP_ADDRESS("Invalid IP address format"),
    INVALID_MAC_ADDRESS("Invalid MAC address format"),
    INVALID_SERIAL_NUMBER("Invalid serial number format"),

    // USER
    INVALID_ROLE("Invalid role format"),
    INVALID_PASSWORD("Password does not meet security requirements"),

    // FILES
    INVALID_FILE_TYPE("Invalid file type"),
    FILE_TOO_LARGE("File size exceeds limit");

    private final String message;

    ResourceFormatExceptionEnum(String message) {
        this.message = message;
    }

    public String build(String value) {
        return message + value;
    }

}
