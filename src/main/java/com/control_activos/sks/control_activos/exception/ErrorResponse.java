package com.control_activos.sks.control_activos.exception;


import java.time.OffsetDateTime;

public record ErrorResponse(
        int statusCode,
        String error,
        String message,
        OffsetDateTime timestamp) {
}
