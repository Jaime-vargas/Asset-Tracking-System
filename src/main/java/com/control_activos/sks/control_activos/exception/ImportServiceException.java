package com.control_activos.sks.control_activos.exception;

import org.springframework.http.HttpStatus;

public class ImportServiceException extends ApiException {

    public  ImportServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
