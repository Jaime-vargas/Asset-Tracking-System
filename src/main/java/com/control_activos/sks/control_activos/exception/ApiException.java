package com.control_activos.sks.control_activos.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException{

    private final HttpStatus status;

    public  ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;

    }
}
