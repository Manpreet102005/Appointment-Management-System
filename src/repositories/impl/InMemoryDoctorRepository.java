package repositories.impl;

import entities.Doctor;
import repositories.DoctorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDoctorRepository implements DoctorRepository {
    ConcurrentHashMap<Integer, Doctor> doctors=new ConcurrentHashMap<>();

    public synchronized boolean addDoctor(Doctor doctor){
        if(doctors.containsKey(doctor.getId())){
            return false;
        }
        doctors.put(doctor.getId(),doctor);
        return true;
    }
    public synchronized boolean removeDoctor(int id){
        if(!doctors.containsKey(id)){
            return false;
        }
        doctors.remove(id);
        return true;
    }
    public Doctor getDoctor(int id){
        return doctors.get(id);
    }
    public List<Doctor> getAllDoctors(){
        List<Doctor> doctorsList=new ArrayList<>();
        for(Doctor doctor:doctors.values()){
            doctorsList.add(doctor);
        }
        return doctorsList;
    }
}
