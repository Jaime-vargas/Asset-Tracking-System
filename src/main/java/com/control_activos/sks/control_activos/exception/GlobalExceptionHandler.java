package com.control_activos.sks.control_activos.exception;

import com.control_activos.sks.control_activos.enums.DuplicateResourceExceptionEnum;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?>  DataIntegrityViolationException(DataIntegrityViolationException ex) {
        Pattern getError = Pattern.compile("'([^']*)'");
        Matcher matcher = getError.matcher(ex.getMessage());
        String duplicated = matcher.find() ? matcher.group(1):"";

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                DuplicateResourceExceptionEnum.DUPLICATE_RESOURCE.build(duplicated),
                OffsetDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> ApiException(ApiException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().value(),
                ex.getStatus().name(),
                ex.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }


}
