package com.example.appointment_management_system.doctor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/doctor")
public class AdminDoctorController {
    private DoctorService doctorService;

    public AdminDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping()
    public ResponseEntity<String> addDoctor(@RequestBody Doctor doctor){
        return doctorService.addDoctor(doctor);
    }
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Integer doctorId){
        return doctorService.deleteDoctor(doctorId);
    }
}
