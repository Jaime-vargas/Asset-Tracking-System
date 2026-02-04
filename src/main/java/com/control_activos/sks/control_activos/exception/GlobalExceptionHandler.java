package com.control_activos.sks.control_activos.exception;

import com.control_activos.sks.control_activos.enums.DuplicateResourceExceptionEnum;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
                HttpStatus.BAD_REQUEST.value(),
                DuplicateResourceExceptionEnum.DUPLICATE_RESOURCE_EXCEPTION.getMessage(duplicated));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundResourceException(NotFoundResourceException ex){
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedResourceException(DuplicatedResourceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(ResourceFormatException.class)
    public ResponseEntity<ErrorResponse> handleFormattResourceExeption(ResourceFormatException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

}
