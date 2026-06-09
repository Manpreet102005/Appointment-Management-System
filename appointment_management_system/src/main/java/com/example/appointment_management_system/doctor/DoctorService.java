package com.example.appointment_management_system.doctor;

import com.example.appointment_management_system.doctor.exceptions.DoctorNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public ResponseEntity<String> addDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return ResponseEntity.ok().body("Doctor Added Successfully. Assiged Id: "+doctor.getDoctorId());
    }
    public ResponseEntity<String> deleteDoctor(Integer doctorId){
        if(!doctorRepository.existsById(doctorId)){
            throw new DoctorNotExistException(doctorId);
        }
        doctorRepository.deleteById(doctorId);
        return ResponseEntity.ok().body("Doctor Removed Successfully");
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Integer doctorId){
        return doctorRepository.findById(doctorId).orElseThrow(()->
                new DoctorNotExistException(doctorId));
    }
}
