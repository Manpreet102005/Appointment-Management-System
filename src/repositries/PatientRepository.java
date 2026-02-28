package repositries;

import entities.Patient;

import java.util.List;

public interface PatientRepository {
    public void addPatient(Patient patient);
    public List<Patient> getAllPatients();
    public Patient getPatient(int id);
    public void deletePatient(int id);
}
