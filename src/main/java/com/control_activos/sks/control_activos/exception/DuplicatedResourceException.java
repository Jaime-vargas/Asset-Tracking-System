package com.control_activos.sks.control_activos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicatedResourceException extends RuntimeException {

    private final int statusCode = HttpStatus.CONFLICT.value();

    public DuplicatedResourceException(String message) {
        super(message);
    }
}
