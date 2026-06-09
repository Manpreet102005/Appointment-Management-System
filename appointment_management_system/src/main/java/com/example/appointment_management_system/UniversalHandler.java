package com.example.appointment_management_system;

import com.example.appointment_management_system.doctor.exceptions.DoctorNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UniversalHandler {
    @ExceptionHandler(DoctorNotExistException.class)
    public ResponseEntity<String> handle(DoctorNotExistException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
