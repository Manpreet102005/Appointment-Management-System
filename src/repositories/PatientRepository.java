package repositories;

import entities.Patient;

import java.util.List;

public interface PatientRepository {
     Patient addPatient(Patient patient);
     List<Patient> getAllPatients();
     Patient getPatient(int id);
     boolean removePatient(int id);
}
