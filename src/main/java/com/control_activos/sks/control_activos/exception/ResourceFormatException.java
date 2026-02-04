package com.control_activos.sks.control_activos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceFormatException extends RuntimeException{
    private final int statusCode = HttpStatus.BAD_REQUEST.value();
    public ResourceFormatException(String message) {
        super(message);
    }

}
