package com.example.appointment_management_system.doctor.exceptions;

public class DoctorNotExistException extends RuntimeException {
    public DoctorNotExistException(Integer doctorId) {
        super("Doctor with id: "+doctorId+" doesn't exist");
    }
}
