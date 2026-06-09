package com.example.appointment_management_system.doctor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/doctor")
public class UserDoctorController {
    private final DoctorService doctorService;

    public UserDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping()
    public List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{doctorId}")
    public Doctor getDoctorById(@PathVariable Integer doctorId){
        return doctorService.getDoctorById(doctorId);
    }
}
