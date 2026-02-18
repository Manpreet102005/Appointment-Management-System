package repositries.impl;

import entities.Doctor;
import repositries.DoctorRepository;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDoctorRepository implements DoctorRepository {
    ConcurrentHashMap<Integer, Doctor> doctors=new ConcurrentHashMap<>();

    public void addDoctor(Doctor doctor){
        doctors.put(doctor.getId(),doctor);
    }
    public void removeDoctor(int id){
        doctors.remove(id);
    }

}
