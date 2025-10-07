package com.ats.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EnrollmentAlreadyExistsException.class)
    public ResponseEntity<String> handleDataIntegrity(EnrollmentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate enrollment not allowed.");
    }

    @ExceptionHandler(EnrollmentValidationException.class)
    public ResponseEntity<String> handleRuntime(EnrollmentValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class) // fallback
    public ResponseEntity<String> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + ex.getMessage());
    }
}
