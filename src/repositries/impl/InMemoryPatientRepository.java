package repositries.impl;

import entities.Patient;
import repositries.PatientRepository;
import validations.PersonValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatientRepository implements PatientRepository {
    ConcurrentHashMap<Integer, Patient> users=new ConcurrentHashMap<>();

    public synchronized void addUser(Patient patient){
        PersonValidation.validate(patient);
        users.put(patient.getId(), patient);
    }
    public Patient getUser(int id){
        return users.get(id);
    }
    public synchronized void deleteUser(int id){
        users.remove(id);
    }
    public List<Patient> getAllUsers(){
        List<Patient> allPatients =new ArrayList<>();
        for(int id: users.keySet()){
            allPatients.add(users.get(id));
        }
        return allPatients;
    }
}
