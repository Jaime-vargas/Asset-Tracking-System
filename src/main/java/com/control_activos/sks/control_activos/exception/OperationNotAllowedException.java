package com.control_activos.sks.control_activos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OperationNotAllowedException extends ApiException {

    public OperationNotAllowedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
