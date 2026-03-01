package repositries.impl;

import entities.Doctor;
import repositries.DoctorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDoctorRepository implements DoctorRepository {
    ConcurrentHashMap<Integer, Doctor> doctors=new ConcurrentHashMap<>();

    public synchronized void addDoctor(Doctor doctor){
        if(doctors.containsKey(doctor.getId())){
            throw  new RuntimeException("Doctor with this id already exists");
        }
        doctors.put(doctor.getId(),doctor);
    }
    public synchronized void removeDoctor(int id){
        doctors.remove(id);
    }
    public Doctor getDoctor(int id){
        return doctors.get(id);
    }
    public List<Doctor> getAllDoctors(){
        List<Doctor> doctorsList=new ArrayList<>();
        for(int id:doctors.keySet()){
            doctorsList.add(doctors.get(id));
        }
        return doctorsList;
    }
}
