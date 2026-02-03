package com.control_activos.sks.control_activos.exception;

import com.control_activos.sks.control_activos.enums.DuplicateResourceExceptionEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?>  handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        Pattern getError = Pattern.compile("'([^']*)'");
        Matcher matcher = getError.matcher(ex.getMessage());
        String duplicated = matcher.find() ? matcher.group(1):"";

        ErrorResponse errorResponse = new ErrorResponse(
                DuplicateResourceExceptionEnum.DUPLICATE_RESOURCE_EXCEPTION.getStatus(),
                DuplicateResourceExceptionEnum.DUPLICATE_RESOURCE_EXCEPTION.getMessage(duplicated));
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
