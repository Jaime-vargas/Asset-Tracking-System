package com.control_activos.sks.control_activos.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum DuplicateResourceExceptionEnum {

    DUPLICATE_RESOURCE("Duplicated resource: "),

    // CAMERA
    DUPLICATE_CAMERA_ID("Camera with the same id already exists: "),
    DUPLICATE_CAMERA_NAME("Camera with the same name already exists: "),
    DUPLICATE_CAMERA_SERIAL_NUMBER("Camera with the same serial number already exists: "),
    DUPLICATE_CAMERA_MAC_ADDRESS("Camera with the same MAC address already exists: "),
    DUPLICATE_CAMERA_IP_ADDRESS("Camera with the same IP address already exists: "),

    // CLIENT
    DUPLICATE_CLIENT_NAME("Client with the same username already exists: "),

    // SUCURSAL

    // USER
    DUPLICATE_USERNAME("Username already exists: "),
    DUPLICATE_USER_NAME("Name already exists: "),

    // REPORT
    DUPLICATE_OPEN_REPORT("There is already an open report for this hardware: ");

    private final String message;

    DuplicateResourceExceptionEnum(String message) {
        this.message = message;
    }

    public String build(String value) {
        return message + value;
    }

}
