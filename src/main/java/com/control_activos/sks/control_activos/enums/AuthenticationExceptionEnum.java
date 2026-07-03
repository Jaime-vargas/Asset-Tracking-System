package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum AuthenticationExceptionEnum {

    INVALID_CREDENTIALS("Invalid username or password"),
    TOKEN_EXPIRED("Authentication token expired"),
    TOKEN_INVALID("Invalid authentication token"),

    // Permissions and roles;
    FORBIDDEN_ACCESS("You do not have permission to access this resource");

    private final String message;

    AuthenticationExceptionEnum(String message) {
        this.message = message;
    }
}