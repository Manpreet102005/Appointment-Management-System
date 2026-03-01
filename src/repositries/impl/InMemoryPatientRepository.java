package repositries.impl;

import entities.Patient;
import repositries.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatientRepository implements PatientRepository {
    ConcurrentHashMap<Integer, Patient> patients =new ConcurrentHashMap<>();

    public synchronized void addPatient(Patient patient){
        if(patients.containsKey(patient.getId())){
            throw new RuntimeException("User with this id already exists.");
        }
        patients.put(patient.getId(), patient);
    }
    public Patient getPatient(int id){
        return patients.get(id);
    }
    public List<Patient> getAllPatients(){
        List<Patient> allPatients =new ArrayList<>();
        for(int id: patients.keySet()){
            allPatients.add(patients.get(id));
        }
        return allPatients;
    }
}
