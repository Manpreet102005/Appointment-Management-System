package repositries;

import entities.Patient;

import java.util.List;

public interface PatientRepository {
    public void addUser(Patient patient);
    public List<Patient> getAllUsers();
    public Patient getUser(int id);
    public void deleteUser(int id);
}
