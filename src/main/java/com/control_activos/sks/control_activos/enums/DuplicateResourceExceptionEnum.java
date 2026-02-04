package com.control_activos.sks.control_activos.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum DuplicateResourceExceptionEnum {
    DUPLICATE_RESOURCE_EXCEPTION("Duplicated resource: "),

    // CAMERA EXCEPTIONS
    DUPLICATE_CAMERA_ID_EXCEPTION("Camera with the same camera ID already exists: "),
    DUPLICATE_CAMERA_NAME_EXCEPTION("Camera with the same name already exists: "),
    DUPLICATE_CAMERA_SERIAL_NUMBER_EXCEPTION("Camera with the same serial number already exists: "),
    DUPLICATE_CAMERA_MAC_ADDRESS_EXCEPTION("Camera with the same MAC address already exists: "),
    DUPLICATE_CAMERA_IP_ADDRESS_EXCEPTION("Camera with the same IP address already exists: ");

    private final String message;

    DuplicateResourceExceptionEnum (String message) {
        this.message = message;
    }

    public String getMessage(String message) {
        return this.message + message;
    }
}
