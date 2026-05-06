package repositories.impl;

import entities.Patient;
import repositories.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatientRepository implements PatientRepository {
    private static int patientId = 0;
    ConcurrentHashMap<Integer, Patient> patients =new ConcurrentHashMap<>();

    public synchronized boolean addPatient(Patient patient){
        patientId++;
        patient.setId(patientId);
        patients.put(patient.getId(), patient);
        return true;
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
    public boolean removePatient(int id){
        if(!patients.containsKey(id)){
            System.out.println("Patient with this id doesn't exists.");
            return false;
        }
        patients.remove(id);
        return true;
    }
}
