package com.control_activos.sks.control_activos.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DuplicateResourceExceptionEnum {
    DUPLICATE_RESOURCE_EXCEPTION(HttpStatus.CONFLICT.value(), "Duplicated resource: ");

    private final int status;
    private final String message;

    DuplicateResourceExceptionEnum (int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage(String duplicated) {
        return this.message + duplicated;
    }
}
