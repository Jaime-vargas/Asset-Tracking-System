package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum ResourceNotFoundExceptionEnum {

    RESOURCE_NOT_FOUND("Resource not found: "),

    // GENERIC
    ID_NOT_FOUND("Resource with id not found: "),

    // ENTITIES
    CLIENT_NOT_FOUND("Client not found with id: "),
    SUCURSAL_NOT_FOUND("Sucursal not found with id: "),
    CAMERA_NOT_FOUND("Camera not found with id: "),
    HARDWARE_NOT_FOUND("Hardware not found with id: "),
    REPORT_NOT_FOUND("Report not found with id: "),
    COMMENT_NOT_FOUND("Comment not found with id: "),
    PHOTO_NOT_FOUND("Photo not found with id: "),
    USER_NOT_FOUND("User not found with id: ");

    private final String message;

    ResourceNotFoundExceptionEnum(String message) {
        this.message = message;
    }

    public String build(Object value) {
        return message + value;
    }

}