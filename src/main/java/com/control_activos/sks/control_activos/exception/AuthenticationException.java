package com.control_activos.sks.control_activos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends ApiException {

    public AuthenticationException(HttpStatus status, String message) {
        super(message, status);
    }
}
