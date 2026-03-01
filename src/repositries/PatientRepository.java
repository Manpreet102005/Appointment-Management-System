package repositries;

import entities.Patient;

import java.util.List;

public interface PatientRepository {
     void addPatient(Patient patient);
     List<Patient> getAllPatients();
     Patient getPatient(int id);

}
