package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum AuthenticationExceptionEnum {

    INVALID_CREDENTIALS("Invalid username or password"),
    TOKEN_EXPIRED("Authentication token expired"),
    TOKEN_INVALID("Invalid authentication token");

    private final String message;

    AuthenticationExceptionEnum(String message) {
        this.message = message;
    }
}