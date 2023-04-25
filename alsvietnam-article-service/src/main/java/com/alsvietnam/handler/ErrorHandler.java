package com.alsvietnam.handler;

import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.models.wrapper.ValidationErrorsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 8:37 PM
 */

@RestControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    protected ResponseEntity<?> handleResponseStatusException(ServiceException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getStatus())
                .body(ObjectResponseWrapper.builder().status(0)
                        .message(e.getReason())
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<?> handleInternalException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ObjectResponseWrapper.builder().status(0)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ObjectResponseWrapper.builder().status(0)
                        .message("You don't have permission to access this resource")
                        .build());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        String message = e.getMessage();
        if (message.equals("Bad credentials") || message.contains("not found")) {
            message = "Invalid username or password";
        }
        log.error(message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ObjectResponseWrapper.builder().status(0)
                        .message(message)
                        .build());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(ValidationErrorsWrapper.builder().status(0)
                        .message(errors)
                        .build());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(ValidationErrorsWrapper.builder().status(0)
                        .message(errors)
                        .build());
    }
}
