package com.nvrsocial.market.exception;

import com.nvrsocial.market.dto.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException exception,
                                                                  HttpServletRequest request) {
        int status = exception.getStatusCode().value();
        String message = exception.getReason() == null ? "Request failed" : exception.getReason();

        return ResponseEntity.status(status).body(
                new ApiError(Instant.now(), status, message, request.getRequestURI(), Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException exception,
                                                              HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> fields.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Validation failed",
                        request.getRequestURI(), fields));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadableException(HttpMessageNotReadableException exception,
                                                               HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Invalid request body",
                        request.getRequestURI(), Map.of()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityException(DataIntegrityViolationException exception,
                                                                 HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiError(Instant.now(), HttpStatus.CONFLICT.value(), "Data conflicts with existing data",
                        request.getRequestURI(), Map.of()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceException(NoResourceFoundException exception,
                                                              HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError(Instant.now(), HttpStatus.NOT_FOUND.value(), "Resource not found",
                        request.getRequestURI(), Map.of()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ApiError(Instant.now(), HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not allowed",
                        request.getRequestURI(), Map.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error",
                        request.getRequestURI(), Map.of()));
    }
}
