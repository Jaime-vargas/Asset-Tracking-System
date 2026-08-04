package com.control_activos.sks.control_activos.exception;

import com.control_activos.sks.control_activos.enums.DuplicateResourceExceptionEnum;
import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                OperationNotAllowedExceptionEnum.USER_PERMISSION_NOT_ALLOWED.getMessage(),
                OffsetDateTime.now()
        );
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(MissingServletRequestPartException e){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                OperationNotAllowedExceptionEnum.NOT_A_MULTIPART_REQUEST.getMessage(),
                OffsetDateTime.now()
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                OperationNotAllowedExceptionEnum.NOT_A_MULTIPART_REQUEST.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?>  MaxUploadSizeExceededException (MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONTENT_TOO_LARGE.value(),
                HttpStatus.CONTENT_TOO_LARGE.name(),
                OperationNotAllowedExceptionEnum.FILE_TOO_LARGE.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

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
