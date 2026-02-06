package com.control_activos.sks.control_activos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceFormatException extends ApiException {

    public ResourceFormatException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
